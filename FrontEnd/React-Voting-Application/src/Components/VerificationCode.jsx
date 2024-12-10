import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

const VerificationCode = ({ email, onCodeSubmit, onCodeVerify }) => {
  const [code, setCode] = useState("");
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [isTimeOver, setIsTimeOver] = useState(false);
  const [timeLeft, setTimeLeft] = useState(0);
  const [showAlert, setShowAlert] = useState(false);

  const handleDismiss = () => {
    setShowAlert(false); // Hide the alert
  };

  const handleGetCode = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8082/users/getCode", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userEmail: email,
        }),
      });

      if (response.ok) {
        const data = await response.json();
        if (onCodeSubmit) onCodeSubmit(data); // Notify parent about success
        setSuccessMessage("Code sent successfully. Check your email.");
        setTimeLeft(180);
        alert("Code sent successfully. Check your email.");
        console.log("Submitted code request successfully:", data);
      } else {
        const errorMessage = await response.text();
        setError(errorMessage || "Submission failed. Please try again.");
      }
    } catch (err) {
      console.error("Error during code submission:", err);
      setError("An error occurred. Please try again later.");
      alert(error);
    }
  };

  const handleVerifyCode = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8082/users/verifyCode", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: email,
          inputtedCode: code,
        }),
      });

      if (response.ok) {
        const data = await response.json();
        if (onCodeVerify) onCodeVerify(data); // Notify parent about success
        setSuccessMessage("Verification successful!");
        alert("Verification successful!");
        console.log("Verification successful:", data);
      } else {
        const errorMessage = await response.text();
        setError(errorMessage || "Verification failed. Please try again.");
        alert("Error Verifying your Code");
      }
    } catch (err) {
      console.error("Error during verification:", err);
      setError("An error occurred. Please try again later.");
      alert("An error occured");
    }
  };

  useEffect(() => {
    if (timeLeft > 0) {
      const timer = setInterval(() => {
        setTimeLeft((prevTime) => prevTime - 1);
      }, 1000);
      return () => clearInterval(timer); // Cleanup on unmount
    } else if (timeLeft === 0) {
      setIsTimeOver(true);
      alert("Time is over, Generate a new Code");
    }
  }, [timeLeft]);

  const formatTime = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, "0")}`;
  };

  return (
    <section>
      <div className="auth">
        <h1>Verify Your Identity</h1>
        <form onSubmit={handleGetCode}>
          <p>Email: {email}</p>
          <button type="submit">Get Code</button>
        </form>
        <form onSubmit={handleVerifyCode}>
          <input
            type="text"
            name="code"
            id="code"
            placeholder="Enter code"
            value={code}
            onChange={(e) => setCode(e.target.value)}
            required
          />
          <button type="submit">Verify</button>
        </form>
        {timeLeft > 0 && (
          <h4 style={{ color: "blue" }}>
            Time remaining for Code Expiration: {formatTime(timeLeft)}
          </h4>
        )}
      </div>
    </section>
  );
};

export default VerificationCode;

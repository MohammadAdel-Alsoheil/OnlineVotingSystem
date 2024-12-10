import React, { useState } from "react";

const LoginCompany = ({ onSignIn, onEmailSubmit }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8082/users/loginToVote", {
        // update this based on your environment
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const data = await response.json();
        if (onSignIn) onSignIn(data); // Notify parent about success
        if (onEmailSubmit) onEmailSubmit(email);
        console.log("Login successful:", data);
      } else {
        const errorMessage = await response.text();
        setError(errorMessage || "Login failed. Please try again.");
      }
    } catch (err) {
      console.error("Error during login:", err);
      setError("An error occurred. Please try again later.");
    }
  };

  return (
    <section>
      <div className="auth">
        <h1>Login To Vote</h1>
        <h3>
          PLease Enter your Company Cridentials, expect a{" "}
          <span style={{ color: "red" }}>SECRET</span> code to reach your email
          after you Login.
        </h3>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="email"
            id="email"
            autoComplete="off"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            name="password"
            id="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit">Login</button>
        </form>
        {error && (
          <p style={{ color: "red" }}>
            You Already Voted or have entered wrong Cridentials
          </p>
        )}
      </div>
    </section>
  );
};

export default LoginCompany;

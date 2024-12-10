import { useState, useEffect } from "react";
import { ethers } from "ethers";
import { contractAbi, contractAddress } from "./Constant/constant";
import Login from "./Components/Login";
import Finished from "./Components/Finished";
import Connected from "./Components/Connected";
import LoginCompany from "./Components/LoginCompany";
import "./App.css";
import VerificationCode from "./Components/VerificationCode";

function App() {
  const [provider, setProvider] = useState(null);
  const [account, setAccount] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [showVerification, setShowVerification] = useState(false);
  const [votingStatus, setVotingStatus] = useState(true);
  const [remainingTime, setremainingTime] = useState("");
  const [candidates, setCandidates] = useState([]);
  const [number, setNumber] = useState("");
  const [CanVote, setCanVote] = useState(true);
  const [isSignedIn, setIsSignedIn] = useState(false);
  const [email, setEmail] = useState("");

  // Store the initial account to lock it
  const [initialAccount, setInitialAccount] = useState(null);

  useEffect(() => {
    getCandidates();
    getRemainingTime();
    getCurrentStatus();
    if (window.ethereum) {
      window.ethereum.on("accountsChanged", handleAccountsChanged);
    }

    return () => {
      if (window.ethereum) {
        window.ethereum.removeListener(
          "accountsChanged",
          handleAccountsChanged
        );
      }
    };
  });

  useEffect(() => {
    if (isSignedIn && !isConnected) {
      connectToMetamask();
    }
  }, [isSignedIn, isConnected]);

  //new
  const handleSignIn = () => {
    setIsSignedIn(true);
    setShowVerification(true);
    console.log("I am signed in");
  };
  const handleEmail = (userEmail) => {
    console.log(email);
    setEmail(userEmail);
  };

  async function vote() {
    try {
      const provider = new ethers.providers.Web3Provider(window.ethereum);
      await provider.send("eth_requestAccounts", []);
      const signer = provider.getSigner();
      const contractInstance = new ethers.Contract(
        contractAddress,
        contractAbi,
        signer
      );

      const tx = await contractInstance.vote(number);
      await tx.wait();

      const response = await fetch("http://localhost:8082/users/IVoted", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: email,
          votingState: true,
        }),
      });

      if (response.ok) {
        console.log("Voting state updated successfully.");
      } else {
        console.log("Failed to vote");
      }
      canVote();
    } catch (e) {
      console.log("An error has occured");
    }
  }

  // async function canVote() {
  //   console.log(voteStatus);
  //   const provider = new ethers.providers.Web3Provider(window.ethereum);
  //   await provider.send("eth_requestAccounts", []);
  //   const signer = provider.getSigner();
  //   const contractInstance = new ethers.Contract(
  //     contractAddress,
  //     contractAbi,
  //     signer
  //   );
  //   const voteStatus = await contractInstance.voters(await signer.getAddress());
  //   setCanVote(voteStatus);
  // }

  async function canVote() {
    try {
      const provider = new ethers.providers.Web3Provider(window.ethereum);
      const signer = provider.getSigner();
      const contractInstance = new ethers.Contract(
        contractAddress,
        contractAbi,
        signer
      );
      const address = await signer.getAddress();
      const voteStatus = await contractInstance.voters(address);

      if (CanVote !== voteStatus) {
        setCanVote(voteStatus);
      }
    } catch (error) {
      console.error("Error checking voting eligibility:", error);
    }
  }

  async function getCandidates() {
    const provider = new ethers.providers.Web3Provider(window.ethereum);
    await provider.send("eth_requestAccounts", []);
    const signer = provider.getSigner();
    const contractInstance = new ethers.Contract(
      contractAddress,
      contractAbi,
      signer
    );
    const candidatesList = await contractInstance.getAllVotesOfCandiates();
    const formattedCandidates = candidatesList.map((candidate, index) => {
      return {
        index: index,
        name: candidate.name,
        voteCount: candidate.voteCount.toNumber(),
      };
    });
    setCandidates(formattedCandidates);
  }

  async function getCurrentStatus() {
    const provider = new ethers.providers.Web3Provider(window.ethereum);
    await provider.send("eth_requestAccounts", []);
    const signer = provider.getSigner();
    const contractInstance = new ethers.Contract(
      contractAddress,
      contractAbi,
      signer
    );
    const status = await contractInstance.getVotingStatus();
    console.log("status: " + status);
    setVotingStatus(status);
  }

  async function getRemainingTime() {
    const provider = new ethers.providers.Web3Provider(window.ethereum);
    await provider.send("eth_requestAccounts", []);
    const signer = provider.getSigner();
    const contractInstance = new ethers.Contract(
      contractAddress,
      contractAbi,
      signer
    );
    const time = await contractInstance.getRemainingTime();
    setremainingTime(parseInt(time, 16));
  }

  // function handleAccountsChanged(accounts) {
  //   if (accounts.length > 0 && account !== accounts[0]) {
  //     setAccount(accounts[0]);
  //     canVote();
  //   } else {
  //     setIsConnected(false);
  //     setAccount(null);
  //   }
  // }

  function handleAccountsChanged(accounts) {
    if (accounts.length > 0) {
      if (initialAccount && accounts[0] !== initialAccount) {
        // If user tries to change accounts, do nothing
        console.log("Account change is disabled.");
        return;
      }
      setAccount(accounts[0]);
      canVote();
    } else {
      setIsConnected(false);
      setAccount(null);
    }
  }

  async function connectToMetamask() {
    if (window.ethereum) {
      try {
        const provider = new ethers.providers.Web3Provider(window.ethereum);
        setProvider(provider);
        console.log(provider);
        await provider.send("eth_requestAccounts", []);
        const signer = provider.getSigner();
        const address = await signer.getAddress();
        setAccount(address);
        setInitialAccount(address); // new
        console.log("Metamask Connected : " + address);
        setIsConnected(true);
        canVote();
      } catch (err) {
        console.error(err);
      }
    } else {
      console.error("Metamask is not detected in the browser");
    }
  }

  async function handleNumberChange(e) {
    setNumber(e.target.value);
  }

  return (
    <div className="App">
      {votingStatus ? (
        !isSignedIn ? (
          <LoginCompany onSignIn={handleSignIn} onEmailSubmit={handleEmail} />
        ) : showVerification ? (
          <VerificationCode
            email={email}
            onCodeSubmit={(data) => {
              console.log("Code submission success:", data);
              setShowVerification(true);
              setIsConnected(true); // Proceed to connected state
            }}
            onCodeVerify={(data) => {
              console.log("Code verification success:", data);
              setShowVerification(false);
              setIsConnected(true); // Proceed to connected state
            }}
          />
        ) : isConnected ? (
          <Connected
            account={account}
            candidates={candidates}
            remainingTime={remainingTime}
            number={number}
            handleNumberChange={handleNumberChange}
            voteFunction={vote}
            showButton={CanVote}
          />
        ) : (
          <Login connectWallet={connectToMetamask} />
        )
      ) : (
        <Finished />
      )}
    </div>
  );
}

export default App;

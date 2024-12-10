async function main() {
  const Voting = await ethers.getContractFactory("Voting");
  try {
    const response = await fetch("http://localhost:8082/users/getCandidates");

    if (!response.ok) {
      throw new Error(`Error fetching candidates: ${response.status}`);
    }

    const candidates = await response.json();

    const candidateNames = candidates.map((candidate) => candidate.name);
    console.log(candidateNames);

    // Start deployment, returning a promise that resolves to a contract object
    const Voting_ = await Voting.deploy(candidateNames, 180);
    console.log("Contract address:", Voting_.address);
  } catch (error) {
    console.error("An error occured ", error);
  }
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });

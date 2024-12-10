# OnlineVotingSystem

This application is a fully deployable online voting system using decentralized architecture powered by blockchain. It leverages the **Volta Energy Testnet**, **MetaMask**, **Spring Boot**, **ReactJS**, and **MongoDB**.

## Backend

1. **Run the Application**  
   Run the application from the `OnlineVotingSystemApplication` class (preferably using **IntelliJ IDEA** or your preferred Java IDE).
   
2. **Run MongoDB Server**  
   Make sure your **MongoDB** server is up and running locally or on a remote server.

3. **Add Test Users using Postman**  
   Use Postman to add test users by sending a `POST` request to the following endpoint: http://localhost:8082/users/add
4. change contact email in UserEmailService



Example request body:
```json
{
    "name": "john",
    "email": "john@gmail.com",
    "age": 20,
    "password": "John@12345",
    "party": "left",
    "isCandidate": true
}

```
Configure Email for 2-Factor Authentication
In the application.properties file, add your email and App Password (NOT your real password) for 2-Factor Authentication:
`spring.mail.username=YOUR_MAIL
spring.mail.password=YOUR_APP_PASSWORD`

## Frontend

To set up the frontend, follow these steps:

1. **Navigate to the React Frontend Directory**  
   Open a terminal and navigate to the React frontend directory:
   ```bash
   cd FrontEnd/React-VotingApplication
2. Install Dependencies Run the following commands to install the required dependencies:
   ```bash
   npm install
   npm install bootstrap
3. Create a .env file in the root of your project directory and add the following configurations:
   `API_URL=https://volta-rpc.energyweb.org` <br>
   `PRIVATE_KEY=YOUR_METAMASK_PRIVATE_KEY`
4. Compile and Deploy the Contract ,Compile the smart contract and deploy it to the Volta Testnet using Hardhat
    ```bash
    npx hardhat compile
    npx hardhat run --network volta scripts/deploy.js
5. Update the Contract Address Add the generated contract address to your .env and constant.js files:
   `CONTRACT_ADDRESS=YOUR_GENERATED_ADDRESS` <br>
   `const contractAddress = "YOUR_GENERATED_ADDRESS";`
6. run
   ```bash
   npm start


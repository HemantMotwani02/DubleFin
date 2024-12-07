import React, { useEffect, useState } from "react";
import Header from "../components/header";
import Footer from "../components/footer";
import './Dashboard.css';
import axios from "axios";

const Dashboard = () => {
    const [balance, setBalance] = useState(0);
    const [receiverName, setReceiverName] = useState("");
    const [amount, setAmount] = useState("");
    const [remarks, setRemarks] = useState("");
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [accountNumber, setAccountNumber] = useState("");
    const [transactions, setTransactions] = useState([]);
    const [showForm, setShowForm] = useState(false);

    // Check for user authentication and fetch user details from localStorage
    useEffect(() => {
        const userDetails = localStorage.getItem("userDetails");

        if (!userDetails) {
            alert("You need to log in to access the dashboard.");
            window.location.href = "/login";
            return;
        }

        const user = JSON.parse(userDetails);
        setUsername(user.name);
        setEmail(user.email);
        setAccountNumber(user.account_number);
        setBalance(user.balance);
        // Fetch account details and transactions if needed
        fetchAccountDetails();
        fetchRecentTransactions();
    }, []);

    const fetchAccountDetails = async () => {
        try {
            const token = JSON.parse(localStorage.getItem("userDetails")).token;
            const  data  = await axios.get("http://localhost:5000/api/users/account_details", {
                headers: { Authorization: `Bearer ${token}` },
            });
            console.log("Data kd",data);
    
            // Set the retrieved data to state
            setBalance(data.data.balance);
            setUsername(data.data.name);
            setEmail(data.data.email);
            setAccountNumber(data.data.account_number);
        } catch (error) {
            alert("Error fetching account details. Please log in again.");
            // window.location.href = "/login";
        }
    };
    

    const fetchRecentTransactions = async () => {
        try {
            const token = JSON.parse(localStorage.getItem("userDetails")).token;
            const { data } = await axios.get("http://localhost:5000/api/transactions/recent", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setTransactions(data);
        } catch (error) {
            console.error("Error fetching recent transactions:", error);
        }
    };

    const handleSendMoney = async (e) => {
        e.preventDefault();
        try {
            const token = JSON.parse(localStorage.getItem("userDetails")).token;
            await axios.post(
                "http://localhost:5000/api/transactions/send",
                { account_number: accountNumber, receiverName, amount, remarks },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            alert("Money sent successfully.");
            fetchAccountDetails();
            fetchRecentTransactions();
            setReceiverName("");
            setAmount("");
            setRemarks("");
            setShowForm(false);
        } catch (error) {
            alert("Error during transaction: " + (error.response?.data?.error || error.message));
        }
    };

    return (
        <div style={{ fontFamily: "Arial, sans-serif", margin: "20px" }}>
            <Header username={username} />

            {/* First Row: Account Details and Send Money */}
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    padding: "20px",
                    backgroundColor: "#f8f9fa",
                    borderRadius: "10px",
                    marginBottom: "20px",
                }}
            >
                <div>
                    <h2 style={{ fontSize: "24px" }}>Account Details</h2>
                    <p><strong>Account Number:</strong> {accountNumber}</p>
                    <p><strong>Account Holder Name:</strong> {username}</p>
                    <p><strong>Email:</strong> {email}</p>
                    <p><strong>Balance:</strong> ${balance.toFixed(2)}</p>
                </div>
                <div>
                    <button
                        onClick={fetchAccountDetails}
                        style={{
                            padding: "10px 20px",
                            backgroundColor: "#17a2b8",
                            color: "white",
                            border: "none",
                            borderRadius: "5px",
                            cursor: "pointer",
                            marginRight: "10px",
                        }}
                    >
                        Refresh
                    </button>
                    <button
                        onClick={() => setShowForm(!showForm)}
                        style={{
                            padding: "10px 20px",
                            backgroundColor: "#007bff",
                            color: "white",
                            border: "none",
                            borderRadius: "5px",
                            cursor: "pointer",
                        }}
                    >
                        {showForm ? "Close Form" : "Send Money"}
                    </button>
                </div>
            </div>

            {/* Money Transfer Form */}
            {showForm && (
                <div
                    style={{
                        position: "absolute",
                        top: "50%",
                        left: "50%",
                        transform: "translate(-50%, -50%)",
                        padding: "30px", // Increased padding
                        backgroundColor: "#ffffff",
                        border: "1px solid #ccc",
                        borderRadius: "10px",
                        width: "500px", // Increased width
                    }}
                >
                    <button
                        onClick={() => setShowForm(false)}
                        style={{
                            position: "absolute",
                            top: "10px",
                            right: "10px",
                            backgroundColor: "red", // Red color
                            color: "white", // White X color
                            border: "none",
                            borderRadius: "50%", // Circle shape
                            width: "30px",
                            height: "30px",
                            fontSize: "20px",
                            cursor: "pointer",
                        }}
                        onMouseOver={(e) => (e.target.style.backgroundColor = "darkred")}
                        onMouseOut={(e) => (e.target.style.backgroundColor = "red")}
                    >
                        &times;
                    </button>
                    <h3>Send Money</h3>
                    <form onSubmit={handleSendMoney} style={{ maxWidth: "100%" }}>
                        <div style={{ marginBottom: "10px" }}>
                            <input
                                type="text"
                                placeholder="Recipient Username"
                                value={receiverName}
                                onChange={(e) => setReceiverName(e.target.value)}
                                required
                                style={{
                                    width: "100%",
                                    padding: "10px",
                                    borderRadius: "5px",
                                    border: "1px solid #ccc",
                                }}
                            />
                        </div>
                        <div style={{ marginBottom: "10px" }}>
                            <input
                                type="number"
                                placeholder="Amount"
                                value={amount}
                                onChange={(e) => setAmount(e.target.value)}
                                required
                                style={{
                                    width: "100%",
                                    padding: "10px",
                                    borderRadius: "5px",
                                    border: "1px solid #ccc",
                                }}
                            />
                        </div>
                        <div style={{ marginBottom: "10px" }}>
                            <input
                                type="text"
                                placeholder="Remarks"
                                value={remarks}
                                onChange={(e) => setRemarks(e.target.value)}
                                style={{
                                    width: "100%",
                                    padding: "10px",
                                    borderRadius: "5px",
                                    border: "1px solid #ccc",
                                }}
                            />
                        </div>
                        <button
                            type="submit"
                            style={{
                                width: "100%",
                                padding: "10px",
                                backgroundColor: "#28a745",
                                color: "white",
                                border: "none",
                                borderRadius: "5px",
                                cursor: "pointer",
                            }}
                        >
                            Send
                        </button>
                    </form>
                </div>
            )}

            {/* Second Row: Recent Transactions */}
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    padding: "20px",
                    backgroundColor: "#e2e3e5", // Same background as Announcements
                    borderRadius: "10px",
                    marginBottom: "20px",
                }}
            >
                <div style={{ flex: 1 }}>
                    <h3 style={{ fontSize: "22px" }}>Recent Transactions</h3>
                    <ul style={{ listStyleType: "none", padding: 0 }}>
                        {transactions.length > 0 ? (
                            transactions.slice(0, 5).map((txn, index) => (
                                <li
                                    key={index}
                                    style={{
                                        borderBottom: "1px solid #ddd",
                                        padding: "10px 0",
                                    }}
                                >
                                    <p><strong>To:</strong> {txn.receiverName}</p>
                                    <p><strong>Amount:</strong> ${txn.amount}</p>
                                    <p><strong>Remarks:</strong> {txn.remarks || "N/A"}</p>
                                    <p><strong>Date:</strong> {new Date(txn.date).toLocaleString()}</p>
                                </li>
                            ))
                        ) : (
                            <p>No recent transactions found.</p>
                        )}
                    </ul>
                </div>
                <div
                    style={{
                        flex: 1,
                        padding: "20px",
                        backgroundColor: "#e2e3e5", // Same background as Recent Transactions
                        borderRadius: "10px",
                    }}
                >
                    <h3 style={{ fontSize: "22px" }}>Announcements</h3>
                    <p><strong>New Feature:</strong> Instant Transfer Enabled!</p>
                    <p>Send money faster and more securely with our new instant transfer feature.</p>
                    <p><strong>Upcoming:</strong> Multi-currency support for international transactions coming soon!</p>
                </div>
            </div>

            <Footer />
        </div>
    );
};

export default Dashboard;

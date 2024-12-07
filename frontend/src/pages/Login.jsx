import React, { useState } from "react";
import axios from "axios";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isEmailValid, setIsEmailValid] = useState(false);
    const [isPasswordValid, setIsPasswordValid] = useState(false);

    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const { data } = await axios.post("http://localhost:5000/api/auth/login", { email, password });
            if (data) {
                localStorage.setItem("userDetails", JSON.stringify({
                    id: data.id,
                    name: data.name,
                    email: data.email,
                    account_number: data.account_number,
                    balance: data.balance,
                    token: data.token,
                }));
                alert("Login successful");
                window.location.href = "/";
            }
        } catch (error) {
            alert("Invalid email or password");
        }
    };

    const handleEmailChange = (e) => {
        const value = e.target.value;
        setEmail(value);
        setIsEmailValid(validateEmail(value));
    };

    const handlePasswordChange = (e) => {
        const value = e.target.value;
        setPassword(value);
        setIsPasswordValid(value.length >= 6);
    };

    return (
        <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh", background: "url('/background-img.jpg') no-repeat center center/cover",  }}>
            <div style={{
                backgroundColor: "rgba(255, 255, 255, 0.8)",
                padding: "40px",
                borderRadius: "12px",
                boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
                width: "450px",
                border: "2px solid black"
            }}>
                <div style={{ textAlign: "center", marginBottom: "20px" }}>
                    <img
                        src="/Dublefin-d-removebg-preview.png"
                        alt="Bank Logo"
                        style={{ height: "60px", marginBottom: "10px" }}
                    />
                    <h2 style={{ margin: 0 }}>Welcome Back!</h2>
                    <p style={{ color: "#6c757d" }}>Log in to your account</p>
                </div>
                <form onSubmit={handleSubmit}>
                    <div style={{ marginBottom: "15px" }}>
                        <input
                            type="email"
                            placeholder="Email"
                            value={email}
                            onChange={handleEmailChange}
                            required
                            style={{
                                width: "100%",
                                padding: "10px",
                                borderRadius: "8px",
                                border: email ? (isEmailValid ? "1px solid black" : "1px solid red") : "1px solid black",
                                outline: "none",
                                fontSize: "16px",
                            }}
                        />
                        {!isEmailValid && email && (
                            <small style={{ color: "red" }}>Please enter a valid email address.</small>
                        )}
                    </div>
                    <div style={{ marginBottom: "15px" }}>
                        <input
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={handlePasswordChange}
                            required
                            style={{
                                width: "100%",
                                padding: "10px",
                                borderRadius: "8px",
                                border: password ? (isPasswordValid ? "1px solid black" : "1px solid red") : "1px solid black",
                                outline: "none",
                                fontSize: "16px",
                            }}
                        />
                        {!isPasswordValid && password && (
                            <small style={{ color: "red" }}>Password must be at least 6 characters long.</small>
                        )}
                    </div>
                    <button
                        type="submit"
                        disabled={!isEmailValid || !isPasswordValid}
                        style={{
                            width: "100%",
                            padding: "10px",
                            backgroundColor: isEmailValid && isPasswordValid ? "#0044cc" : "#add8e6",
                            color: "white",
                            border: "none",
                            borderRadius: "8px",
                            cursor: isEmailValid && isPasswordValid ? "pointer" : "not-allowed",
                            fontSize: "16px",
                        }}
                    >
                        Login
                    </button>
                </form>
                <div style={{ textAlign: "center", marginTop: "15px" }}>
                    <p style={{ fontSize: "14px", color: "#6c757d" }}>
                        Don't have an account? <a href="/register" style={{ color: "#0044cc", textDecoration: "none" }}>Create Account</a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;

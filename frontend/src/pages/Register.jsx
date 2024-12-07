import React, { useState } from "react";
import axios from "axios";

const Register = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [acceptConditions, setAcceptConditions] = useState(false);

    const [isNameValid, setIsNameValid] = useState(false);
    const [isEmailValid, setIsEmailValid] = useState(false);
    const [isPasswordValid, setIsPasswordValid] = useState(false);
    const [isConfirmPasswordValid, setIsConfirmPasswordValid] = useState(false);

    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    };

    const validateName = (name) => {
        return name.length >= 3;
    };

    const validatePassword = (password) => {
        return password.length >= 6;
    };

    const validateConfirmPassword = (confirmPassword, password) => {
        return confirmPassword === password;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!acceptConditions) {
            alert("You must accept the terms and conditions to register.");
            return;
        }

        try {
            // const { data } = await axios.post(
            //     "http://localhost:5000/api/auth/register",
            //     {
            //         name,
            //         email,
            //         password,
            //     },
            //     {
            //         headers: {
            //             "Content-Type": "application/json",
            //         },
            //     }
            // );
            const data={
                name: name,
                email: email,
                password: password
            }
            fetch('http://localhost:5000/api/auth/register', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',  // Ensure Content-Type is set
                },
                body: JSON.stringify(data),
              });

            // Log the received data
            console.log(data);

            // Store the required details in localStorage
            localStorage.setItem("userDetails", JSON.stringify({
                id: data.id,
                name: data.name,
                email: data.email,
                account_number: data.account_number,
                balance: data.balance,
                token: data.token,
            }));

            alert("Registration successful. You can now log in.");
            window.location.href = "/"; // Redirect to the home page or login
        } catch (error) {
            console.error("Error during registration:", error);
            alert("Error during registration. Try again with valid details.");
        }
    };


    return (
        <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh", background: "url('/background-img.jpg') no-repeat center center/cover", }}>
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
                        alt="Logo"
                        style={{ height: "60px", marginBottom: "10px" }}
                    />
                    <h2 style={{ margin: 0 }}>Create Your Account</h2>
                    <p style={{ color: "#6c757d" }}>Register to get started</p>
                </div>
                <form onSubmit={handleSubmit}>
                    <div style={{ marginBottom: "15px" }}>
                        <input
                            type="text"
                            placeholder="Name"
                            value={name}
                            onChange={(e) => {
                                const value = e.target.value;
                                setName(value);
                                setIsNameValid(validateName(value));
                            }}
                            required
                            style={{
                                width: "100%",
                                padding: "10px",
                                borderRadius: "8px",
                                border: name ? (isNameValid ? "1px solid black" : "1px solid red") : "1px solid black",
                                outline: "none",
                                fontSize: "16px",
                            }}
                        />
                        {!isNameValid && name && (
                            <small style={{ color: "red" }}>Name must be at least 3 characters long.</small>
                        )}
                    </div>
                    <div style={{ marginBottom: "15px" }}>
                        <input
                            type="email"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => {
                                const value = e.target.value;
                                setEmail(value);
                                setIsEmailValid(validateEmail(value));
                            }}
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
                            onChange={(e) => {
                                const value = e.target.value;
                                setPassword(value);
                                setIsPasswordValid(validatePassword(value));
                            }}
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
                    <div style={{ marginBottom: "15px" }}>
                        <input
                            type="password"
                            placeholder="Confirm Password"
                            value={confirmPassword}
                            onChange={(e) => {
                                const value = e.target.value;
                                setConfirmPassword(value);
                                setIsConfirmPasswordValid(validateConfirmPassword(value, password));
                            }}
                            required
                            style={{
                                width: "100%",
                                padding: "10px",
                                borderRadius: "8px",
                                border: confirmPassword ? (isConfirmPasswordValid ? "1px solid black" : "1px solid red") : "1px solid black",
                                outline: "none",
                                fontSize: "16px",
                            }}
                        />
                        {!isConfirmPasswordValid && confirmPassword && (
                            <small style={{ color: "red" }}>Passwords do not match.</small>
                        )}
                    </div>
                    <div style={{ marginBottom: "15px", textAlign: "left" }}>
                        <label>
                            <input
                                type="checkbox"
                                checked={acceptConditions}
                                onChange={(e) => setAcceptConditions(e.target.checked)}
                                style={{ marginRight: "8px" }}
                            />
                            I accept the <a href="/terms" style={{ color: "#0044cc", textDecoration: "none" }}>terms and conditions</a>
                        </label>
                    </div>
                    <button
                        type="submit"
                        disabled={!(isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid && acceptConditions)}
                        style={{
                            width: "100%",
                            padding: "10px",
                            backgroundColor: isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid && acceptConditions ? "#0044cc" : "#add8e6",
                            color: "white",
                            border: "none",
                            borderRadius: "8px",
                            cursor: isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid && acceptConditions ? "pointer" : "not-allowed",
                            fontSize: "16px",
                        }}
                    >
                        Register
                    </button>
                </form>
                <div style={{ textAlign: "center", marginTop: "15px" }}>
                    <p style={{ fontSize: "14px", color: "#6c757d" }}>
                        Already have an account? <a href="/login" style={{ color: "#0044cc", textDecoration: "none" }}>Log in</a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Register;

import React from "react";

const Header = ({ username, onLogout }) => {
    const handleLogout = () => {
        // Clear user-related data from localStorage or session
        localStorage.removeItem("userDetails");

        // Call the onLogout function to notify the parent component (if necessary)
        if (onLogout) {
            onLogout();
        }

        // Redirect to login page
        window.location.href = "/login";
    };

    return (
        <header
            style={{
                padding: "20px",
                backgroundColor: "#0044cc",
                color: "white",
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                textAlign: "left",
            }}
        >
            <div style={{ display: "flex", alignItems: "center" }}>
                <img
                    src="../Dublefin-d-removebg-preview-removebg-preview (1).png"
                    alt="DUBLEFIN"
                    style={{
                        width: "70px",
                        height: "70px",
                        borderRadius: "50%",
                        marginRight: "10px", // space between the image and the heading
                    }}
                />
                <h2 style={{ margin: 0 }}>Simple. Fast. Secure</h2>
            </div>
            <div style={{ display: "flex", alignItems: "center" }}>
                <p style={{ fontSize: "20px", margin: 0 }}>Welcome, {username}</p> {/* Increased font size */}
                <button
                    onClick={handleLogout}
                    style={{
                        marginLeft: "20px",
                        padding: "10px 20px",
                        backgroundColor: "#ff4d4d",
                        color: "white",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                    }}
                >
                    Logout
                </button>
            </div>
        </header>
    );
};

export default Header;

// Footer.js
import React from "react";

const Footer = () => {
    return (
        <footer style={{ padding: "20px", backgroundColor: "#0044cc", color: "white", textAlign: "center" }}>
            <div>
                <ul style={{
                    listStyleType: "none", 
                    padding: 0, 
                    margin: 0, 
                    display: "flex",  // Align items in a row
                    justifyContent: "center", // Center the links
                }}>
                    <li style={{ margin: "0 15px" }}><a href="#" style={{ color: "white", textDecoration: "none" }}>About Us</a></li>
                    <li style={{ margin: "0 15px" }}><a href="#" style={{ color: "white", textDecoration: "none" }}>Contact Us</a></li>
                    <li style={{ margin: "0 15px" }}><a href="#" style={{ color: "white", textDecoration: "none" }}>Terms of Use</a></li>
                    <li style={{ margin: "0 15px" }}><a href="#" style={{ color: "white", textDecoration: "none" }}>Privacy Policy</a></li>
                </ul>
            </div>
            <div style={{ display: "flex", alignItems: "center", justifyContent: "center", marginTop: "20px" }}>
                <img 
                    src="../Dublefin-d-removebg-preview-removebg-preview (1).png" 
                    alt="DUBLEFIN" 
                    style={{ width: "40px", height: "40px", borderRadius: "50%", marginRight: "10px" }} 
                />
                <p>&copy; 2024 DUBLEFIN Bank. All rights reserved.</p>
            </div>
        </footer>
    );
};

export default Footer;

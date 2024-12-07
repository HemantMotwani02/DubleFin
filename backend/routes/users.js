// routes/user.js
const express = require("express");
const router = express.Router();
const { User } = require("../models");
const jwt = require("jsonwebtoken");
const JWT_SECRET = process.env.JWT_SECRET;

// Middleware to verify JWT token
const authenticate = (req, res, next) => {
    const token = req.header("Authorization")?.replace("Bearer ", "");

    if (!token) {
        return res.status(401).json({ message: "Authentication token missing." });
    }

    try {
        const decoded = jwt.verify(token, JWT_SECRET);
        req.user = decoded; // Add user information from token to the request
        next();
    } catch (error) {
        if (error.name === 'TokenExpiredError') {
            return res.status(401).json({ message: "Token has expired." });
        }
        return res.status(401).json({ message: "Invalid or expired token." });
    }
};

// Route to fetch account details
router.get("/account_details", authenticate, async (req, res) => {
    try {
        console.log("request: ",req.user.id);
        const user = await User.findOne({ where: { id: req.user.id } });

        if (!user) {
            return res.status(404).json({ message: "User not found." });
        }

        const { name, email, balance, account_number } = user;
        res.json({ name, email, balance, account_number });
    } catch (error) {
console.log("wew");
       
        console.error(error);
        res.status(500).json({ message: "Error fetching account details." });
    }
});

module.exports = router;

const express = require("express");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const { User } = require("../models");

const router = express.Router();
const JWT_SECRET = process.env.JWT_SECRET;

// Register
router.post("/register", async (req, res) => {
    console.log("Registering user...", req.body);
    const { name, email, password } = req.body;
    const hashedPassword = await bcrypt.hash(password, 10);

    if (!name || !email || !password) {
        return res.status(400).json({ error: "All fields are required." });
    }

    const existingUser = await User.findOne({ where: { email } });
    if (existingUser) {
        return res.status(400).json({ error: "Email is already registered." });
    }
    // Generate a random 11-digit account number
    const generateAccountNumber = () => {
        return Math.floor(10000000000 + Math.random() * 90000000000).toString();
    };

    let accountNumber;
    do {
        accountNumber = generateAccountNumber();
    } while (await User.findOne({ where: { account_number: accountNumber } }));

    try {


        // User creation logic
        const user = await User.create({
            name,
            password: hashedPassword,
            balance: 0, // Initial balance as float
            createdAt: new Date(), // Creation timestamp
            updatedAt: new Date(), // Update timestamp
            account_number: accountNumber, // Random 11-digit account number
            email,
        });
        if (user) {
            // Generate a JWT token with a 1-day expiry
            const token = jwt.sign({ id: user.id, email: user.email }, 'your_secret_key', {
                expiresIn: '1d', // Token expires in 1 day
            });

            // Send only required fields in the response
            res.status(200).json({
                id: user.id,
                name: user.name,
                email: user.email,
                account_number: user.account_number,
                balance: user.balance,
                token,
            });
        }
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// Login
router.post("/login", async (req, res) => {
    const { email, password } = req.body;
    const user = await User.findOne({ where: { email:email } });
    if (!user || !(await bcrypt.compare(password, user.password))) {
        return res.status(401).json({ error: "Invalid credentials" });
    }
    const token = jwt.sign({ id: user.id }, JWT_SECRET, { expiresIn: "1d" });
    res.status(200).json({
        id: user.id,
        name: user.name,
        email: user.email,
        account_number: user.account_number,
        balance: user.balance,
        token,
    });
});

module.exports = router;

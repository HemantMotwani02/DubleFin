// app.js
const express = require("express");
const cors = require("cors");
const env = require("dotenv").config();
const bodyParser = require("body-parser");
const { sequelize } = require("./models");

const app = express();
app.use(cors({ origin: 'http://localhost:3000' }));
app.use(express.json()); // Crucial for parsing JSON requests
app.use(express.urlencoded({ extended: true })); // For form data (optional)

const authRoutes = require("./routes/auth");
const transactionRoutes = require("./routes/transactions");
const userRoutes = require("./routes/users");

app.use("/api/auth", authRoutes);
app.use("/api/transactions", transactionRoutes);
app.use("/api/users", userRoutes);

// Sync database (only in development)
if (process.env.NODE_ENV !== 'production') {
    sequelize.sync({ alter: true });
}

// Start server
const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`));

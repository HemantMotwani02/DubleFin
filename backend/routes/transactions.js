const express = require("express");
const { User, Transaction } = require("../models");
const { sequelize } = require("../models");
const router = express.Router();

// Send money
router.post("/send", async (req, res) => {
    const { account_number, receiverName, amount, remarks } = req.body;
    const t = await sequelize.transaction();

    try {
        const sender = await User.findOne({ where: { account_number: account_number }, transaction: t });
        const receiver = await User.findOne({ where: { name: receiverName }, transaction: t });

        if (!receiver || sender.balance < amount) {
            throw new Error("Invalid transaction");
        }

        sender.balance -= amount;
        receiver.balance += amount;

        await sender.save({ transaction: t });
        await receiver.save({ transaction: t });

        // Assuming you already have `sender`, `receiver`, and `amount` defined in your code

        // Create a new transaction record
        await Transaction.create({
            senderId: sender.id,                   // Assuming sender has an 'id' field
            receiverId: receiver.id,               // Assuming receiver has an 'id' field
            sender_account_number: sender.account_number, // Assuming sender has 'account_number'
            receiver_account_number: receiver.account_number, // Assuming receiver has 'account_number'
            amount: amount,
            remarks: remarks || null,               // If remarks are optional, pass null if not provided
            transactionType: "credit",       // Should be either 'credit' or 'debit'
        }, { transaction: t });                    // Using the transaction (t) if it's part of a transaction


        await t.commit();
        res.status(200).json({ message: "Transaction successful" });
    } catch (error) {
        await t.rollback();
        res.status(400).json({ error: error.message });
    }
});

module.exports = router;

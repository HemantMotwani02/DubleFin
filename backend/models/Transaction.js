const { DataTypes } = require("sequelize");

module.exports = (sequelize) => {
    return sequelize.define("Transaction", {
        id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
        senderId: { type: DataTypes.INTEGER, allowNull: false },
        receiverId: { type: DataTypes.INTEGER, allowNull: false },
        sender_account_number: { type: DataTypes.STRING, allowNull: false },
        receiver_account_number: { type: DataTypes.STRING, allowNull: false },
        amount: { type: DataTypes.FLOAT, allowNull: false },
        remarks: { type: DataTypes.TEXT, allowNull: true }, // Allow null if remarks are optional
        transactionType: {
            type: DataTypes.ENUM,
            values: ['credit', 'debit'],
            allowNull: false, // Ensure this field cannot be null
        },
    });
};

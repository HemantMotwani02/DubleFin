const { Sequelize } = require("sequelize");

const sequelize = new Sequelize("banking_db", "root", "pass", {
    host: "localhost",
    dialect: "mysql",
});

const User = require("./User")(sequelize);
const Transaction = require("./Transaction")(sequelize);

User.hasMany(Transaction, { foreignKey: "senderId" });
User.hasMany(Transaction, { foreignKey: "receiverId" });

module.exports = { sequelize, User, Transaction };

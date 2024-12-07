const { DataTypes } = require("sequelize");

module.exports = (sequelize) => {
    return sequelize.define("User", {
        id: { type: DataTypes.INTEGER, autoIncrement: true, primaryKey: true },
        account_number: { type: DataTypes.STRING, allowNull: false, unique: true },
        name: { type: DataTypes.STRING, allowNull: false },
        email: { type: DataTypes.STRING(320), allowNull: false, unique: true },
        password: { type: DataTypes.STRING, allowNull: false },
        balance: { type: DataTypes.FLOAT, allowNull: true, defaultValue: 3000 },
    });
};

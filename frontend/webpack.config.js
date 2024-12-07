const webpack = require("webpack");

module.exports = {
    resolve: {
        fallback: {
            http: require.resolve("stream-http"),
            https: require.resolve("https-browserify"),
            zlib: require.resolve("browserify-zlib"),
            util: require.resolve("util/"),
            buffer: require.resolve("buffer/"),
        },
    },
    plugins: [
        new webpack.ProvidePlugin({
            Buffer: ["buffer", "Buffer"],
        }),
    ],
};

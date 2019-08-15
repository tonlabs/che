const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: path.resolve(__dirname, 'src/main/js/ton.js'),
    output: {
        path: path.resolve(__dirname, 'target/public'),
        filename: 'ton.js',
    },
    plugins: [
        new webpack.optimize.LimitChunkCountPlugin({
            maxChunks: 1, // disable creating additional chunks
        })
    ],
    mode: 'development'
};

const path = require('path');
const webpack = require('webpack');

module.exports = {
  entry: path.resolve(__dirname, 'src/main/js/hello_world.js'),
  output: {
    path: path.resolve(__dirname,
        '../../../ide/che-ide-gwt-app/src/main/resources/org/eclipse/che/ide/public'),
    filename: 'ton.js',
    library: "TonSdk",
    libraryTarget: "amd",
  },
  plugins: [
    new webpack.optimize.LimitChunkCountPlugin({
      maxChunks: 1, // disable creating additional chunks
    })
  ],
  mode: 'development'
};

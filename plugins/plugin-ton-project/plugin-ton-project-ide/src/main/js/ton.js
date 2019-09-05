import TONClientLibrary from 'ton-sdk-wasm';
import {TONClient} from 'ton-sdk-js';

export class TonSdk {
  constructor() {
    TONClient.setLibrary(TONClientLibrary);
  }

  async runContract(node, address, functionName, abi, input, keyPair) {
    var ton = new TONClient();

    ton.config.setData({
      servers: [node],
      log_verbose: true
    });

    ton.setup().then(function() {
      var obj = {
        functionName,
        address,
        abi,
        input,
        keyPair,
      };
      console.log(obj);
      return this.ton.contracts.run(obj);
    });
  }
}

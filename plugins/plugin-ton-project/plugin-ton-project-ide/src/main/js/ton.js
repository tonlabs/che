import TONClientLibrary from 'ton-sdk-wasm';
import {TONClient} from 'ton-sdk-js';

export class TonSdk {
  constructor() {
    TONClient.setLibrary(TONClientLibrary);
  }

  async runContract(node, address, functionName, abi, input, keyPair) {
    let ton = new TONClient();

    ton.config.setData({
      servers: [node],
      log_verbose: true
    });

    ton.setup().then(function() {
      let obj = {
        functionName,
        address,
        abi,
        input,
        keyPair,
      };
      console.log(obj);
      return ton.contracts.run(obj);
    });
  }
}

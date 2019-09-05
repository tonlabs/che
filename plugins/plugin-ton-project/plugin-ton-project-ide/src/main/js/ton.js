import TONClientLibrary from 'ton-sdk-wasm';
import {TONClient} from 'ton-sdk-js';

export class TonSdk {
  constructor() {
  }

  async runContract(node, address, functionName, abi, input, keyPair) {
    var ton = new TONClient();
    TONClient.setLibrary(TONClientLibrary);
    ton.setup().then(function(){
      ton.config.setData({
        servers: [node],
        log_verbose: true
      });
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

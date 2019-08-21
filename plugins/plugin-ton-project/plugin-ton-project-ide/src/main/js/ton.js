import TONClientLibrary from 'ton-sdk-wasm';
import {TONClient} from 'ton-sdk-js';

export class TonSdk {
  constructor() {
    this.ton = TONClient.shared;
    TONClient.setLibrary(TONClientLibrary);
    this.ton.config.setData({
      servers: ['services.tonlabs.io'],
      log_verbose: true
    });
  }

  async setup() {
    return this.ton.setup();
  }

  async runContract(address, functionName, abi, input, keyPair) {
    return this.ton.contracts.run({
      functionName,
      address,
      abi,
      input,
      keyPair,
    });
  }
}

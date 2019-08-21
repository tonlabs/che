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

  sendMessage() {
    alert('sendMessage');
  }
}

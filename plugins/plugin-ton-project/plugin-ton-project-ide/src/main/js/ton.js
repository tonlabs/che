import TONClientLibrary from 'ton-sdk-wasm';
import {TONClient} from 'ton-sdk-js';

export class TonSdk {
  constructor() {
    this.tonConfig = {
      servers: ['services.tonlabs.io'],
      log_verbose: true
    };
    this.ton = TONClient.shared;
  }

  async initTon() {
    TONClient.setLibrary(TONClientLibrary);
    this.ton.config.setData(this.tonConfig);
    await this.ton.setup();
  }

  async initApp() {
    await this.initTon();
  }

  sendMessage() {
    alert('sendMessage');
  }
}

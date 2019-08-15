import TONClientLibrary from 'ton-sdk-wasm';
import { TONClient } from 'ton-sdk-js';

class TonSDK {
  constructor() {
    this.tonConfig = {
      servers: ['services.tonlabs.io'],
      log_verbose: true
    };
  }

  async initTon() {
    const ton = TONClient.shared;
    TONClient.setLibrary(TONClientLibrary);
    ton.config.setData(this.tonConfig);
    await ton.setup();
  }

  async initApp() {
    await this.initTon();
  }

  init() {
    this.initApp().then(() => {
      alert('init JsInterop ok');
    });
  }
}
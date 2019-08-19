import TONClientLibrary from 'ton-sdk-wasm';
import {TONClient} from 'ton-sdk-js';

class TonSDK {
  constructor(tonConfig) {
    this.tonConfig = tonConfig;
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

  init() {
    this.initApp().then(() => {
      alert('TonSDK init ok');
    });
  }

  sendMessage() {
    alert('sendMessage');
  }
}

const tonSdk = new TonSDK({
  servers: ['services.tonlabs.io'],
  log_verbose: true
});

tonSdk.init();
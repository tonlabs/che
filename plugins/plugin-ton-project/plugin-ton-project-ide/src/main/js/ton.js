import TONClientLibrary from 'ton-sdk-wasm';
import { TONClient } from 'ton-sdk-js';

const tonConfig = {
  servers: ['services.tonlabs.io'],
  log_verbose: true
};

async function initTon() {
  const ton = TONClient.shared;
  TONClient.setLibrary(TONClientLibrary);
  ton.config.setData(tonConfig);
  await ton.setup();
}

async function initApp() {
  await initTon();
}

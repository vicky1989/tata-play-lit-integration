// Skip ExceptionsManager init (temporary workaround)
global.__fbDisableExceptionsManager = true;

// Make sure ErrorUtils exists even if InitializeCore is slow to set it
global.ErrorUtils = global.ErrorUtils || {
  setGlobalHandler: () => {},
  getGlobalHandler: () => () => {},
};

import 'react-native/Libraries/Core/InitializeCore';
import { AppRegistry } from 'react-native';
import LitScreen from './index';

AppRegistry.registerComponent('LitScreen', () => LitScreen);

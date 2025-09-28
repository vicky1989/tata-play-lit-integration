import React from 'react';
import { WebView } from 'react-native-webview';

export default function LitScreen() {
  return (
    <WebView
      source={{ uri: 'https://www.google.com' }}
      originWhitelist={['*']}
      javaScriptEnabled
      domStorageEnabled
      onError={(e) => console.log('WV onError:', e.nativeEvent)}
      onHttpError={(e) => console.log('WV onHttpError:', e.nativeEvent)}
    />
  );
}



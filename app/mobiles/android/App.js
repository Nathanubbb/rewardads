import React, { useRef, useState, useEffect } from 'react';
import { StyleSheet, View, StatusBar, BackHandler } from 'react-native';
import { WebView } from 'react-native-webview';

export default function App() {
  const webviewRef = useRef(null);
  const [canGoBack, setCanGoBack] = useState(false);

  const handleBackPress = () => {
    if(canGoBack && webviewRef.current) {
      webviewRef.current.goBack();
      return true;
    }
    
    return false;
  };

  useEffect(() => {
    BackHandler.addEventListener('hardwareBackPress', handleBackPress);

    return () => {
      BackHandler.removeEventListener('hardwareBackPress', handleBackPress);
    };
  }, [canGoBack]);

  return (
    <View style={styles.container}>
      <StatusBar backgroundColor="#ff9800" barStyle="light-content" />
      <WebView
        ref={webviewRef}
        source={{ uri: 'https://app.rewardads.it' }}
        style={styles.webview}
        onNavigationStateChange={(navState) => {
          setCanGoBack(navState.canGoBack);
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  toolbar: {
    height: 50,
    backgroundColor: 'orange',
    justifyContent: 'center',
    alignItems: 'center',
  },
  toolbarText: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
  webview: {
    flex: 1,
  },
});
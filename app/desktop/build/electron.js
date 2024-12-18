const { app, BrowserWindow } = require('electron');
const axios = require('axios');
const fs = require('fs');
const path = require('path');

const ICON_URL = 'https://storage.rewardads.it/defaults/rewardads-logo-rounded.png';
const ICON_PATH = path.join(app.getPath('userData'), 'downloaded-icon.png');

let mainWindow;

async function downloadIcon() {
    try {
        const response = await axios({
            url: ICON_URL,
            method: 'GET',
            responseType: 'arraybuffer',
        });

        fs.writeFileSync(ICON_PATH, response.data);
        console.log('Icon downloaded successfully');
    } catch(error) {
        console.error('Error downloading the icon:', error);
    }
}

async function createWindow() {
    await downloadIcon();

    mainWindow = new BrowserWindow({
        width: 1280,
        height: 720,
        icon: ICON_PATH,
        webPreferences: {
            nodeIntegration: false,
            contextIsolation: true,
        },
        backgroundColor: '#ffffff',
    });

    mainWindow.setMenuBarVisibility(false);
    mainWindow.loadURL('https://app.rewardads.it');
}

app.whenReady().then(() => {
    createWindow();

    app.on('activate', () => {
        if(BrowserWindow.getAllWindows().length === 0) {
            createWindow();
        }
    });
});

app.on('window-all-closed', () => {
    if(process.platform !== 'darwin') {
        app.quit();
    }
});

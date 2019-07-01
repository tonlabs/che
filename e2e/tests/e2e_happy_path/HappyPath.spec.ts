/*********************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 **********************************************************************/

import { e2eContainer } from '../../inversify.config';
import { DriverHelper } from '../../utils/DriverHelper';
import { CLASSES } from '../../inversify.types';
import { Ide, RightToolbarButton } from '../../pageobjects/ide/Ide';
import { ProjectTree } from '../../pageobjects/ide/ProjectTree';
import { TopMenu } from '../../pageobjects/ide/TopMenu';
import { QuickOpenContainer } from '../../pageobjects/ide/QuickOpenContainer';
import { Editor } from '../../pageobjects/ide/Editor';
import { PreviewWidget } from '../../pageobjects/ide/PreviewWidget';
import { GitHubPlugin } from '../../pageobjects/ide/GitHubPlugin';
import { TestConstants } from '../../TestConstants';
import { RightToolbar } from '../../pageobjects/ide/RightToolbar';
import { By, Key } from 'selenium-webdriver';
import { Terminal } from '../../pageobjects/ide/Terminal';
import { DebugView } from '../../pageobjects/ide/DebugView';
import { WarningDialog } from '../../pageobjects/ide/WarningDialog';

const driverHelper: DriverHelper = e2eContainer.get(CLASSES.DriverHelper);
const ide: Ide = e2eContainer.get(CLASSES.Ide);
const projectTree: ProjectTree = e2eContainer.get(CLASSES.ProjectTree);
const topMenu: TopMenu = e2eContainer.get(CLASSES.TopMenu);
const quickOpenContainer: QuickOpenContainer = e2eContainer.get(CLASSES.QuickOpenContainer);
const editor: Editor = e2eContainer.get(CLASSES.Editor);
const previewWidget: PreviewWidget = e2eContainer.get(CLASSES.PreviewWidget);
const githubPlugin: GitHubPlugin = e2eContainer.get(CLASSES.GitHubPlugin);
const rightToolbar: RightToolbar = e2eContainer.get(CLASSES.RightToolbar);
const terminal: Terminal = e2eContainer.get(CLASSES.Terminal);
const debugView: DebugView = e2eContainer.get(CLASSES.DebugView);
const warningDialog: WarningDialog = e2eContainer.get(CLASSES.WarningDialog);

const projectName: string = 'petclinic';
const namespace: string = TestConstants.TS_SELENIUM_USERNAME;
const workspaceName: string = TestConstants.TS_SELENIUM_HAPPY_PATH_WORKSPACE_NAME;
const workspaceUrl: string = `${TestConstants.TS_SELENIUM_BASE_URL}/dashboard/#/ide/${namespace}/${workspaceName}`;
const pathToJavaFolder: string = `${projectName}/src/main/java/org/springframework/samples/petclinic`;
const pathToChangedJavaFileFolder: string = `${projectName}/src/main/java/org/springframework/samples/petclinic/system`;
const javaFileName: string = 'PetClinicApplication.java';
const changedJavaFileName: string = 'CrashController.java';
const textForErrorMessageChange: string = 'HHHHHHHHHHHHH';
const codeNavigationClassName: string = 'SpringApplication.class';
const pathToYamlFolder: string = projectName;
const yamlFileName: string = 'devfile.yaml';
const expectedGithubChanges: string = '_remote.repositories %3F/.m2/repository/antlr/antlr/2.7.7\n' + 'U';

const SpringAppLocators = {
    springTitleLocator: By.xpath('//div[@class=\'container-fluid\']//h2[text()=\'Welcome\']'),
    springMenuButtonLocator: By.css('button[data-target=\'#main-navbar\']'),
    springErrorButtonLocator: By.xpath('//div[@id=\'main-navbar\']//span[text()=\'Error\']'),
    springErrorMessageLocator: By.xpath('//p[text()=\'Expected: controller used to ' +
        `showcase what happens when an exception is thrown${textForErrorMessageChange}\']`),

};


suite('Ide checks', async () => {
    test('Open workspace', async () => {
        await driverHelper.navigateTo(workspaceUrl);
    });

    test('Wait workspace running state', async () => {
        await ide.waitWorkspaceAndIde(namespace, workspaceName);
    });

    test('Build application', async () => {
        await driverHelper.navigateTo(workspaceUrl);
        await ide.waitWorkspaceAndIde(namespace, workspaceName);
        await projectTree.openProjectTreeContainer();
        await projectTree.waitProjectImported(projectName, 'src');
        await projectTree.expandItem(`/${projectName}`);
        await topMenu.waitTopMenu();
        await ide.closeAllNotifications();
        await topMenu.clickOnTopMenuButton('Terminal');
        await topMenu.clickOnSubmenuItem('Run Task...');
        await quickOpenContainer.clickOnContainerItem('che: build-file-output');

        await projectTree.expandPathAndOpenFile(projectName, 'build-output.txt');
        await editor.waitEditorAvailable('build-output.txt');
        await editor.clickOnTab('build-output.txt');
        await editor.waitTabFocused('build-output.txt');
        await editor.followAndWaitForText('build-output.txt', '[INFO] BUILD SUCCESS', 180000, 5000);
    });

    test('Run application', async () => {
        await topMenu.waitTopMenu();
        await ide.closeAllNotifications();
        await topMenu.clickOnTopMenuButton('Terminal');
        await topMenu.clickOnSubmenuItem('Run Task...');
        await quickOpenContainer.clickOnContainerItem('che: run');

        await ide.waitNotification('A new process is now listening on port 8080', 120000);
        await ide.clickOnNotificationButton('A new process is now listening on port 8080', 'yes');

        await ide.waitNotification('Redirect is now enabled on port 8080', 120000);
        await ide.clickOnNotificationButton('Redirect is now enabled on port 8080', 'Open Link');
        await previewWidget.waitContentAvailable(SpringAppLocators.springTitleLocator, 60000, 10000);
        await rightToolbar.clickOnToolIcon('Preview');
        await previewWidget.waitPreviewWidgetAbsence();
        await terminal.closeTerminalTab('build-file-output');

        await terminal.rejectTerminalProcess('run');
        await terminal.closeTerminalTab('run');

        await warningDialog.waitAndCloseIfAppear();
    });

    test('Java LS initialization', async () => {
        await projectTree.expandPathAndOpenFile(pathToJavaFolder, javaFileName);
        await editor.waitEditorAvailable(javaFileName);
        await editor.clickOnTab(javaFileName);
        await editor.waitTabFocused(javaFileName);

        await ide.checkLSInitializationStart('Starting Java Language Server');
        await ide.waitStatusBarTextAbcence('Starting Java Language Server', 360000);
        await checkJavaPathCompletion();
        await ide.waitStatusBarTextAbcence('Building workspace', 360000);


        await editor.waitEditorAvailable(javaFileName);
        await editor.clickOnTab(javaFileName);
        await editor.waitTabFocused(javaFileName);
        await editor.moveCursorToLineAndChar(javaFileName, 32, 27);
        await editor.pressControlSpaceCombination(javaFileName);
        await editor.waitSuggestion(javaFileName, 'run(Class<?> primarySource, String... args) : ConfigurableApplicationContext', 40000);
    });

    test('Error highlighting', async () => {
        await editor.type(javaFileName, 'textForErrorHighlighting', 30);
        await editor.waitErrorInLine(30);
        await editor.performKeyCombination(javaFileName, Key.chord(Key.CONTROL, 'z'));
        await editor.waitErrorInLineDisappearance(30);

    });

    test('Autocomplete and suggestion', async () => {
        await editor.moveCursorToLineAndChar(javaFileName, 32, 17);
        await editor.pressControlSpaceCombination(javaFileName);
        await editor.waitSuggestionContainer();
        await editor.waitSuggestion(javaFileName, 'SpringApplication - org.springframework.boot');


        await editor.moveCursorToLineAndChar(javaFileName, 32, 27);
        await editor.pressControlSpaceCombination(javaFileName);
        await editor.waitSuggestion(javaFileName, 'run(Class<?> primarySource, String... args) : ConfigurableApplicationContext');
    });


    test('Codenavigation', async () => {
        await editor.moveCursorToLineAndChar(javaFileName, 32, 17);
        await editor.performKeyCombination(javaFileName, Key.chord(Key.CONTROL, Key.F12));
        await editor.waitEditorAvailable(codeNavigationClassName);
    });


    test('Display source code changes in the running application', async () => {
        console.log('===>>>> 1');
        await projectTree.expandPathAndOpenFile(pathToChangedJavaFileFolder, changedJavaFileName);
        console.log('===>>>> 2');
        await editor.waitEditorAvailable(changedJavaFileName);
        console.log('===>>>> 3');
        await editor.clickOnTab(changedJavaFileName);
        console.log('===>>>> 4');
        await editor.waitTabFocused(changedJavaFileName);
        console.log('===>>>> 5');


        await editor.moveCursorToLineAndChar(changedJavaFileName, 34, 55);
        console.log('===>>>> 6');
        await editor.performKeyCombination(changedJavaFileName, textForErrorMessageChange);
        console.log('===>>>> 7');
        await editor.performKeyCombination(changedJavaFileName, Key.chord(Key.CONTROL, 's'));
        console.log('===>>>> 8');


        await topMenu.clickOnTopMenuButton('Terminal');
        console.log('===>>>> 9');
        await topMenu.clickOnSubmenuItem('Run Task...');
        console.log('===>>>> 10');
        await quickOpenContainer.clickOnContainerItem('che: build');
        console.log('===>>>> 11');



        await projectTree.expandPathAndOpenFile(projectName, 'build.txt');
        console.log('===>>>> 12');
        await editor.waitEditorAvailable('build.txt');
        console.log('===>>>> 13');
        await editor.clickOnTab('build.txt');
        console.log('===>>>> 14');
        await editor.waitTabFocused('build.txt');
        console.log('===>>>> 15');
        await editor.followAndWaitForText('build.txt', '[INFO] BUILD SUCCESS', 180000, 5000);
        console.log('===>>>> 16');



        await topMenu.clickOnTopMenuButton('Terminal');
        console.log('===>>>> 17');
        await topMenu.clickOnSubmenuItem('Run Task...');
        console.log('===>>>> 18');
        await quickOpenContainer.clickOnContainerItem('che: run');
        console.log('===>>>> 19');



        await ide.waitNotification('A new process is now listening on port 8080', 120000);
        console.log('===>>>> 20');
        await ide.clickOnNotificationButton('A new process is now listening on port 8080', 'yes');
        console.log('===>>>> 21');

        await ide.waitNotification('Redirect is now enabled on port 8080', 120000);
        console.log('===>>>> 22');
        await ide.clickOnNotificationButton('Redirect is now enabled on port 8080', 'Open Link');
        console.log('===>>>> 23');

        await previewWidget.waitContentAvailable(SpringAppLocators.springTitleLocator, 60000, 10000);
        console.log('===>>>> 24');

        await previewWidget.waitAndSwitchToWidgetFrame();
        console.log('===>>>> 25');
        await previewWidget.waitAndClick(SpringAppLocators.springMenuButtonLocator);
        console.log('===>>>> 26');
        await previewWidget.waitAndClick(SpringAppLocators.springErrorButtonLocator);
        console.log('===>>>> 27');
        await previewWidget.waitVisibility(SpringAppLocators.springErrorMessageLocator);
        console.log('===>>>> 28');
        await previewWidget.switchBackToIdeFrame();
        console.log('===>>>> 29');

        await rightToolbar.clickOnToolIcon('Preview');
        console.log('===>>>> 30');
        await previewWidget.waitPreviewWidgetAbsence();
        console.log('===>>>> 31');

        await terminal.rejectTerminalProcess('run');
        console.log('===>>>> 32');
        await terminal.closeTerminalTab('run');
        console.log('===>>>> 33');

        await warningDialog.waitAndCloseIfAppear();
        console.log('===>>>> 34');
    });

    test('Debug', async () => {
        console.log('===>>>   1');
        await projectTree.expandPathAndOpenFile(pathToJavaFolder, javaFileName);
        console.log('===>>>   2');
        await editor.selectTab(javaFileName);
        console.log('===>>>   3');
        await editor.moveCursorToLineAndChar(javaFileName, 34, 1);
        console.log('===>>>   4');
        await editor.activateBreakpoint(javaFileName, 32);
        console.log('===>>>   5');

        await topMenu.clickOnTopMenuButton('Terminal');
        console.log('===>>>   6');
        await topMenu.clickOnSubmenuItem('Run Task...');
        console.log('===>>>   7');
        await quickOpenContainer.clickOnContainerItem('che: run-debug');
        console.log('===>>>   8');



        await ide.waitNotification('A new process is now listening on port 8080', 120000);
        console.log('===>>>   9');
        await ide.clickOnNotificationButton('A new process is now listening on port 8080', 'yes');
        console.log('===>>>   10');

        await ide.waitNotification('Redirect is now enabled on port 8080', 120000);
        console.log('===>>>   11');
        await ide.clickOnNotificationButton('Redirect is now enabled on port 8080', 'Open Link');
        console.log('===>>>   12');

        await previewWidget.waitContentAvailable(SpringAppLocators.springErrorMessageLocator, 60000, 10000);
        console.log('===>>>   13');

        await ide.closeAllNotifications();


        // ####################################



        console.log('===>>>   15');
        await topMenu.clickOnTopMenuButton('Debug');
        console.log('===>>>   16');
        await topMenu.clickOnSubmenuItem('Open Configurations');
        console.log('===>>>   17');
        await editor.waitEditorAvailable('launch.json');
        console.log('===>>>   18');
        await editor.selectTab('launch.json');
        console.log('===>>>   19');

        await editor.moveCursorToLineAndChar('launch.json', 5, 22);
        console.log('===>>>   20');
        await editor.performKeyCombination('launch.json', Key.chord(Key.CONTROL, Key.SPACE));
        console.log('===>>>   21');
        await editor.clickOnSuggestion('Java: Launch Program in Current File');
        console.log('===>>>   22');
        await editor.waitTabWithUnsavedStatus('launch.json');
        console.log('===>>>   23');
        await editor.waitText('launch.json', '\"name\": \"Debug (Launch) - Current File\"');
        console.log('===>>>   24');
        await editor.performKeyCombination('launch.json', Key.chord(Key.CONTROL, 's'));
        console.log('===>>>   25');
        await editor.waitTabWithSavedStatus('launch.json');
        console.log('===>>>   26');


        // ####################################

        console.log('===>>>   27');
        await editor.selectTab(javaFileName);
        console.log('===>>>   28');

        await topMenu.clickOnTopMenuButton('View');
        console.log('===>>>   29');
        await topMenu.clickOnSubmenuItem('Debug');
        console.log('===>>>   30');

        await ide.waitRightToolbarButton(RightToolbarButton.Debug);
        console.log('===>>>   31');
        await debugView.clickOnDebugConfigurationDropDown();
        console.log('===>>>   32');
        await debugView.clickOnDebugConfigurationItem('Debug (Launch) - Current File');
        console.log('===>>>   33');
        await debugView.clickOnRunDebugButton();
        console.log('===>>>   34');

        await previewWidget.refreshPage();
        console.log('===>>>   35');
        await editor.waitStoppedDebugBreakpoint(javaFileName, 32);
        console.log('===>>>   36');
    });


    test.skip('Yaml LS initialization', async () => {
        await projectTree.expandPathAndOpenFile(pathToYamlFolder, yamlFileName);
        await editor.waitEditorAvailable(yamlFileName);
        await editor.clickOnTab(yamlFileName);
        await editor.waitTabFocused(yamlFileName);
        await ide.waitStatusBarContains('Starting Yaml Language Server');
        await ide.waitStatusBarContains('100% Starting Yaml Language Server');
        await ide.waitStatusBarTextAbcence('Starting Yaml Language Server');
    });

    test.skip('Github plugin initialization', async () => {
        await githubPlugin.openGitHubPluginContainer();
        await githubPlugin.waitChangesPresence(expectedGithubChanges);
    });
});

async function checkJavaPathCompletion() {
    if (await ide.isNotificationPresent('Classpath is incomplete. Only syntax errors will be reported')) {
        throw new Error('Known issue: https://github.com/eclipse/che/issues/13427 \n' +
            '\"Java LS \"Classpath is incomplete\" warning when loading petclinic\"');
    }
}

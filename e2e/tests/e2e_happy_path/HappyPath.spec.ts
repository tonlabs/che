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
import { Ide } from '../../pageobjects/ide/Ide';
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
// const debugView: DebugView = e2eContainer.get(CLASSES.DebugView);

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
// const debugConfigurationFile: string = 'launch.json';

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
        terminal.closeTerminalTab('run');
    });

    test('Java LS initialization', async () => {
        await projectTree.expandPathAndOpenFile(pathToJavaFolder, javaFileName);
        await editor.waitEditorAvailable(javaFileName);
        await editor.clickOnTab(javaFileName);
        await editor.waitTabFocused(javaFileName);

        await ide.waitStatusBarTextAbcence('Starting Java Language Server', 360000);
        await editor.moveCursorToLineAndChar(javaFileName, 32, 27);
        await editor.pressControlSpaceCombination(javaFileName);
        await editor.waitSuggestion(javaFileName, 'run(Class<?> primarySource, String... args) : ConfigurableApplicationContext', 40000);
    });

    // ########################################################################
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
        await projectTree.expandPathAndOpenFile(pathToChangedJavaFileFolder, changedJavaFileName);
        await editor.waitEditorAvailable(changedJavaFileName);
        await editor.clickOnTab(changedJavaFileName);
        await editor.waitTabFocused(changedJavaFileName);


        await editor.moveCursorToLineAndChar(changedJavaFileName, 34, 55);
        await editor.performKeyCombination(changedJavaFileName, textForErrorMessageChange);
        await editor.performKeyCombination(changedJavaFileName, Key.chord(Key.CONTROL, 's'));


        await topMenu.clickOnTopMenuButton('Terminal');
        await topMenu.clickOnSubmenuItem('Run Task...');
        await quickOpenContainer.clickOnContainerItem('che: build');



        await projectTree.expandPathAndOpenFile(projectName, 'build.txt');
        await editor.waitEditorAvailable('build.txt');
        await editor.clickOnTab('build.txt');
        await editor.waitTabFocused('build.txt');
        await editor.followAndWaitForText('build.txt', '[INFO] BUILD SUCCESS', 180000, 5000);



        await topMenu.clickOnTopMenuButton('Terminal');
        await topMenu.clickOnSubmenuItem('Run Task...');
        await quickOpenContainer.clickOnContainerItem('che: run');



        await ide.waitNotification('A new process is now listening on port 8080', 120000);
        await ide.clickOnNotificationButton('A new process is now listening on port 8080', 'yes');

        await ide.waitNotification('Redirect is now enabled on port 8080', 120000);
        await ide.clickOnNotificationButton('Redirect is now enabled on port 8080', 'Open Link');

        await previewWidget.waitContentAvailable(SpringAppLocators.springTitleLocator, 60000, 10000);

        await previewWidget.waitAndSwitchToWidgetFrame();
        await previewWidget.waitAndClick(SpringAppLocators.springMenuButtonLocator);
        await previewWidget.waitAndClick(SpringAppLocators.springErrorButtonLocator);
        await previewWidget.waitVisibility(SpringAppLocators.springErrorMessageLocator);
        await previewWidget.switchBackToIdeFrame();

        await rightToolbar.clickOnToolIcon('Preview');
        await previewWidget.waitPreviewWidgetAbsence();

        await terminal.rejectTerminalProcess('run');
        terminal.closeTerminalTab('run');
    });

    // ##################################################################################################
    // ##################################################################################################
    // ##################################################################################################

    test('Debug', async () => {
        // await topMenu.clickOnTopMenuButton('Debug');
        // await topMenu.clickOnSubmenuItem('Open Configurations');

        // await editor.waitEditorAvailable(debugConfigurationFile);
        // await editor.waitTabFocused(debugConfigurationFile);

        // await editor.moveCursorToLineAndChar(debugConfigurationFile, 11, 7);
        // await editor.pressControlSpaceCombination(debugConfigurationFile);

        // await editor.waitSuggestion(debugConfigurationFile, 'Java: Attach');
        // await editor.clickOnSuggestion('Java: Attach');
        // await editor.waitText(debugConfigurationFile, '\"name\": \"Debug (Attach)\"');
        // await editor.moveCursorToLineAndChar(debugConfigurationFile, 16, 17);




        // await editor.performKeyCombination(debugConfigurationFile, Key.chord(Key.SHIFT, Key.END));
        // await editor.performKeyCombination(debugConfigurationFile, Key.DELETE);
        // await editor.performKeyCombination(debugConfigurationFile, '1044');
        // await editor.waitText(debugConfigurationFile, '\"port\": 1044');

        // await editor.waitTabWithUnsavedStatus(debugConfigurationFile);
        // await editor.performKeyCombination(debugConfigurationFile, Key.chord(Key.CONTROL, 's'));
        // await editor.waitTabWithSavedStatus(debugConfigurationFile);



        // await topMenu.clickOnTopMenuButton('View');
        // await topMenu.clickOnSubmenuItem('Debug');

        // await ide.waitRightToolbarButton(RightToolbarButton.Debug);
        // await debugView.clickOnDebugConfigurationDropDown();
        // await debugView.clickOnDebugConfigurationItem('Debug (Launch)-PetClinicApplication<spring-petclinic>');
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

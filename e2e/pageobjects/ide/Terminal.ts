/*********************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 **********************************************************************/
import { injectable, inject } from 'inversify';
import { CLASSES } from '../../inversify.types';
import { DriverHelper } from '../../utils/DriverHelper';
import { By, Key } from 'selenium-webdriver';
import { TestConstants } from '../../TestConstants';

@injectable()
export class Terminal {
    private static readonly TERMINAL_INTERACTION_CONTAINER_CSS_LOCATOR: string = 'textarea[aria-label=\'Terminal input\']';
    constructor(@inject(CLASSES.DriverHelper) private readonly driverHelper: DriverHelper) { }

    async waitTab(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const terminalTabLocator: By = By.css(this.getTerminalTabCssLocator(tabTitle));

        await this.driverHelper.waitVisibility(terminalTabLocator, timeout);
    }

    async waitTabAbsence(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const terminalTabLocator: By = By.css(this.getTerminalTabCssLocator(tabTitle));

        await this.driverHelper.waitDisappearanceWithTimeout(terminalTabLocator, timeout);
    }

    async clickOnTab(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const terminalTabLocator: By = By.css(this.getTerminalTabCssLocator(tabTitle));

        await this, this.driverHelper.waitAndClick(terminalTabLocator, timeout);
    }

    async waitTabFocused(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const focusedTerminalTabLocator: By = this.getFocusedTerminalTabLocator(tabTitle);

        await this.driverHelper.waitVisibility(focusedTerminalTabLocator, timeout);
    }

    async selectTerminalTab(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        await this.clickOnTab(tabTitle, timeout);
        await this.waitTabFocused(tabTitle, timeout);
    }

    async clickOnTabCloseIcon(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const terminalTabCloseIconLocator: By =
            By.css(`${this.getTerminalTabCssLocator(tabTitle)} div.p-TabBar-tabCloseIcon`);

        await this.driverHelper.waitAndClick(terminalTabCloseIconLocator, timeout);
    }

    async closeTerminalTab(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        await this.clickOnTabCloseIcon(tabTitle, timeout);
        await this.waitTabAbsence(tabTitle, timeout);
    }

    async type(text: string) {
        const terminalInteractionContainer: By = By.css(Terminal.TERMINAL_INTERACTION_CONTAINER_CSS_LOCATOR);

        await this.driverHelper.typeToInvisible(terminalInteractionContainer, text);
    }

    async rejectTerminalProcess(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        await this.selectTerminalTab(tabTitle, timeout);
        await this.type(Key.chord(Key.CONTROL, 'c'));

        // for ensure that command performed
        await this.driverHelper.wait(3000);
    }

    private getTerminalTabCssLocator(tabTitle: string): string {
        return `li[title='${tabTitle}']`;
    }

    private getFocusedTerminalTabLocator(tabTitle: string): By {
        return By.css(`li[title='${tabTitle}'].p-mod-current.theia-mod-active`);
    }

}

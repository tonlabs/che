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
import { By } from 'selenium-webdriver';
import { TestConstants } from '../../TestConstants';

@injectable()
export class Terminal {
    constructor(@inject(CLASSES.DriverHelper) private readonly driverHelper: DriverHelper) { }

    async waitTab(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const terminalTabLocator: By = By.xpath(this.getTerminalTabXpathLocator(tabTitle));

        await this.driverHelper.waitVisibility(terminalTabLocator, timeout);
    }

    async waitTabAbsence(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const terminalTabLocator: By = By.xpath(this.getTerminalTabXpathLocator(tabTitle));

        await this.driverHelper.waitDisappearanceWithTimeout(terminalTabLocator, timeout);
    }

    async clickOnTabCloseIcon(tabTitle: string, timeout: number = TestConstants.TS_SELENIUM_DEFAULT_TIMEOUT) {
        const terminalTabCloseIconLocator: By =
            By.xpath(`${this.getTerminalTabXpathLocator(tabTitle)}//div[@class='p-TabBar-tabCloseIcon']`);

        this.driverHelper.waitAndClick(terminalTabCloseIconLocator, timeout);
    }

    private getTerminalTabXpathLocator(tabTitle: string) {
        return `//li[@title='${tabTitle}']`;
    }

}

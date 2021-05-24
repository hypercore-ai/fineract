--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

CREATE TABLE IF NOT EXISTS `m_loan_disbursement_detail_history`
(
    `id`                     BIGINT         NOT NULL AUTO_INCREMENT,
    `loan_id`                BIGINT         NOT NULL,
    `expected_disburse_date` DATETIME       NOT NULL,
    `disbursedon_date`       DATETIME       NULL,
    `principal`              DECIMAL(19, 6) NOT NULL,
    `version`                INT            NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_loan_disbursement_detail_history_loan_id` FOREIGN KEY (`loan_id`) REFERENCES `m_loan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

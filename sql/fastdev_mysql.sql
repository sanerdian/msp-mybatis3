
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for act_app_appdef
-- ----------------------------
DROP TABLE IF EXISTS `act_app_appdef`;
CREATE TABLE `act_app_appdef`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REV_` int(11) NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `RESOURCE_NAME_` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `ACT_IDX_APP_DEF_UNIQ`(`KEY_`, `VERSION_`, `TENANT_ID_`) USING BTREE,
  INDEX `ACT_IDX_APP_DEF_DPLY`(`DEPLOYMENT_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_APP_DEF_DPLY` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_app_deployment` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_app_appdef
-- ----------------------------

-- ----------------------------
-- Table structure for act_app_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_app_databasechangelog`;
CREATE TABLE `act_app_databasechangelog`  (
  `ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_app_databasechangelog
-- ----------------------------
INSERT INTO `act_app_databasechangelog` VALUES ('1', 'flowable', 'org/flowable/app/db/liquibase/flowable-app-db-changelog.xml', '2022-12-09 07:31:59', 1, 'EXECUTED', '8:496fc778bdf2ab13f2e1926d0e63e0a2', 'createTable tableName=ACT_APP_DEPLOYMENT; createTable tableName=ACT_APP_DEPLOYMENT_RESOURCE; addForeignKeyConstraint baseTableName=ACT_APP_DEPLOYMENT_RESOURCE, constraintName=ACT_FK_APP_RSRC_DPL, referencedTableName=ACT_APP_DEPLOYMENT; createIndex...', '', NULL, '3.8.9', NULL, NULL, '0571152741');
INSERT INTO `act_app_databasechangelog` VALUES ('2', 'flowable', 'org/flowable/app/db/liquibase/flowable-app-db-changelog.xml', '2022-12-09 07:32:00', 2, 'EXECUTED', '8:ccea9ebfb6c1f8367ca4dd473fcbb7db', 'modifyDataType columnName=DEPLOY_TIME_, tableName=ACT_APP_DEPLOYMENT', '', NULL, '3.8.9', NULL, NULL, '0571152741');
INSERT INTO `act_app_databasechangelog` VALUES ('3', 'flowable', 'org/flowable/app/db/liquibase/flowable-app-db-changelog.xml', '2022-12-09 07:32:00', 3, 'EXECUTED', '8:f1f8aff320aade831944ebad24355f3d', 'createIndex indexName=ACT_IDX_APP_DEF_UNIQ, tableName=ACT_APP_APPDEF', '', NULL, '3.8.9', NULL, NULL, '0571152741');

-- ----------------------------
-- Table structure for act_app_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_app_databasechangeloglock`;
CREATE TABLE `act_app_databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_app_databasechangeloglock
-- ----------------------------
INSERT INTO `act_app_databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for act_app_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_app_deployment`;
CREATE TABLE `act_app_deployment`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOY_TIME_` datetime(3) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_app_deployment
-- ----------------------------

-- ----------------------------
-- Table structure for act_app_deployment_resource
-- ----------------------------
DROP TABLE IF EXISTS `act_app_deployment_resource`;
CREATE TABLE `act_app_deployment_resource`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `RESOURCE_BYTES_` longblob NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_APP_RSRC_DPL`(`DEPLOYMENT_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_APP_RSRC_DPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_app_deployment` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_app_deployment_resource
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_casedef
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_casedef`;
CREATE TABLE `act_cmmn_casedef`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REV_` int(11) NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `RESOURCE_NAME_` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `HAS_GRAPHICAL_NOTATION_` bit(1) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  `DGRM_RESOURCE_NAME_` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `HAS_START_FORM_KEY_` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `ACT_IDX_CASE_DEF_UNIQ`(`KEY_`, `VERSION_`, `TENANT_ID_`) USING BTREE,
  INDEX `ACT_IDX_CASE_DEF_DPLY`(`DEPLOYMENT_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_CASE_DEF_DPLY` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_cmmn_deployment` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_casedef
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_databasechangelog`;
CREATE TABLE `act_cmmn_databasechangelog`  (
  `ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_databasechangelog
-- ----------------------------
INSERT INTO `act_cmmn_databasechangelog` VALUES ('1', 'flowable', 'org/flowable/cmmn/db/liquibase/flowable-cmmn-db-changelog.xml', '2022-12-09 07:31:44', 1, 'EXECUTED', '8:8b4b922d90b05ff27483abefc9597aa6', 'createTable tableName=ACT_CMMN_DEPLOYMENT; createTable tableName=ACT_CMMN_DEPLOYMENT_RESOURCE; addForeignKeyConstraint baseTableName=ACT_CMMN_DEPLOYMENT_RESOURCE, constraintName=ACT_FK_CMMN_RSRC_DPL, referencedTableName=ACT_CMMN_DEPLOYMENT; create...', '', NULL, '3.8.9', NULL, NULL, '0571133801');
INSERT INTO `act_cmmn_databasechangelog` VALUES ('2', 'flowable', 'org/flowable/cmmn/db/liquibase/flowable-cmmn-db-changelog.xml', '2022-12-09 07:31:46', 2, 'EXECUTED', '8:65e39b3d385706bb261cbeffe7533cbe', 'addColumn tableName=ACT_CMMN_CASEDEF; addColumn tableName=ACT_CMMN_DEPLOYMENT_RESOURCE; addColumn tableName=ACT_CMMN_RU_CASE_INST; addColumn tableName=ACT_CMMN_RU_PLAN_ITEM_INST', '', NULL, '3.8.9', NULL, NULL, '0571133801');
INSERT INTO `act_cmmn_databasechangelog` VALUES ('3', 'flowable', 'org/flowable/cmmn/db/liquibase/flowable-cmmn-db-changelog.xml', '2022-12-09 07:31:47', 3, 'EXECUTED', '8:c01f6e802b49436b4489040da3012359', 'addColumn tableName=ACT_CMMN_RU_PLAN_ITEM_INST; addColumn tableName=ACT_CMMN_RU_CASE_INST; createIndex indexName=ACT_IDX_PLAN_ITEM_STAGE_INST, tableName=ACT_CMMN_RU_PLAN_ITEM_INST; addColumn tableName=ACT_CMMN_RU_PLAN_ITEM_INST; addColumn tableNam...', '', NULL, '3.8.9', NULL, NULL, '0571133801');
INSERT INTO `act_cmmn_databasechangelog` VALUES ('4', 'flowable', 'org/flowable/cmmn/db/liquibase/flowable-cmmn-db-changelog.xml', '2022-12-09 07:31:48', 4, 'EXECUTED', '8:e40d29cb79345b7fb5afd38a7f0ba8fc', 'createTable tableName=ACT_CMMN_HI_PLAN_ITEM_INST; addColumn tableName=ACT_CMMN_RU_MIL_INST; addColumn tableName=ACT_CMMN_HI_MIL_INST', '', NULL, '3.8.9', NULL, NULL, '0571133801');
INSERT INTO `act_cmmn_databasechangelog` VALUES ('5', 'flowable', 'org/flowable/cmmn/db/liquibase/flowable-cmmn-db-changelog.xml', '2022-12-09 07:31:53', 5, 'EXECUTED', '8:70349de472f87368dcdec971a10311a0', 'modifyDataType columnName=DEPLOY_TIME_, tableName=ACT_CMMN_DEPLOYMENT; modifyDataType columnName=START_TIME_, tableName=ACT_CMMN_RU_CASE_INST; modifyDataType columnName=START_TIME_, tableName=ACT_CMMN_RU_PLAN_ITEM_INST; modifyDataType columnName=T...', '', NULL, '3.8.9', NULL, NULL, '0571133801');
INSERT INTO `act_cmmn_databasechangelog` VALUES ('6', 'flowable', 'org/flowable/cmmn/db/liquibase/flowable-cmmn-db-changelog.xml', '2022-12-09 07:31:53', 6, 'EXECUTED', '8:10e82e26a7fee94c32a92099c059c18c', 'createIndex indexName=ACT_IDX_CASE_DEF_UNIQ, tableName=ACT_CMMN_CASEDEF', '', NULL, '3.8.9', NULL, NULL, '0571133801');
INSERT INTO `act_cmmn_databasechangelog` VALUES ('7', 'flowable', 'org/flowable/cmmn/db/liquibase/flowable-cmmn-db-changelog.xml', '2022-12-09 07:31:54', 7, 'EXECUTED', '8:530bc81a1e30618ccf4a2da1f7c6c043', 'renameColumn newColumnName=CREATE_TIME_, oldColumnName=START_TIME_, tableName=ACT_CMMN_RU_PLAN_ITEM_INST; renameColumn newColumnName=CREATE_TIME_, oldColumnName=CREATED_TIME_, tableName=ACT_CMMN_HI_PLAN_ITEM_INST; addColumn tableName=ACT_CMMN_RU_P...', '', NULL, '3.8.9', NULL, NULL, '0571133801');

-- ----------------------------
-- Table structure for act_cmmn_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_databasechangeloglock`;
CREATE TABLE `act_cmmn_databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_databasechangeloglock
-- ----------------------------
INSERT INTO `act_cmmn_databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for act_cmmn_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_deployment`;
CREATE TABLE `act_cmmn_deployment`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOY_TIME_` datetime(3) NULL DEFAULT NULL,
  `PARENT_DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_deployment
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_deployment_resource
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_deployment_resource`;
CREATE TABLE `act_cmmn_deployment_resource`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `RESOURCE_BYTES_` longblob NULL,
  `GENERATED_` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_CMMN_RSRC_DPL`(`DEPLOYMENT_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_CMMN_RSRC_DPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_cmmn_deployment` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_deployment_resource
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_hi_case_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_hi_case_inst`;
CREATE TABLE `act_cmmn_hi_case_inst`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REV_` int(11) NOT NULL,
  `BUSINESS_KEY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `PARENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CASE_DEF_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `STATE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NULL DEFAULT NULL,
  `END_TIME_` datetime(3) NULL DEFAULT NULL,
  `START_USER_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CALLBACK_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CALLBACK_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_hi_case_inst
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_hi_mil_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_hi_mil_inst`;
CREATE TABLE `act_cmmn_hi_mil_inst`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REV_` int(11) NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `TIME_STAMP_` datetime(3) NULL DEFAULT NULL,
  `CASE_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CASE_DEF_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ELEMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_hi_mil_inst
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_hi_plan_item_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_hi_plan_item_inst`;
CREATE TABLE `act_cmmn_hi_plan_item_inst`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REV_` int(11) NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `STATE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CASE_DEF_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CASE_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `STAGE_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `IS_STAGE_` bit(1) NULL DEFAULT NULL,
  `ELEMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `ITEM_DEFINITION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `ITEM_DEFINITION_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_AVAILABLE_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_ENABLED_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_DISABLED_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_STARTED_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_SUSPENDED_TIME_` datetime(3) NULL DEFAULT NULL,
  `COMPLETED_TIME_` datetime(3) NULL DEFAULT NULL,
  `OCCURRED_TIME_` datetime(3) NULL DEFAULT NULL,
  `TERMINATED_TIME_` datetime(3) NULL DEFAULT NULL,
  `EXIT_TIME_` datetime(3) NULL DEFAULT NULL,
  `ENDED_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_UPDATED_TIME_` datetime(3) NULL DEFAULT NULL,
  `START_USER_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `REFERENCE_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `REFERENCE_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  `ENTRY_CRITERION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `EXIT_CRITERION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_hi_plan_item_inst
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_ru_case_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_ru_case_inst`;
CREATE TABLE `act_cmmn_ru_case_inst`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REV_` int(11) NOT NULL,
  `BUSINESS_KEY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `PARENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CASE_DEF_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `STATE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NULL DEFAULT NULL,
  `START_USER_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CALLBACK_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CALLBACK_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  `LOCK_TIME_` datetime(3) NULL DEFAULT NULL,
  `IS_COMPLETEABLE_` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_CASE_INST_CASE_DEF`(`CASE_DEF_ID_`) USING BTREE,
  INDEX `ACT_IDX_CASE_INST_PARENT`(`PARENT_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_CASE_INST_CASE_DEF` FOREIGN KEY (`CASE_DEF_ID_`) REFERENCES `act_cmmn_casedef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_ru_case_inst
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_ru_mil_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_ru_mil_inst`;
CREATE TABLE `act_cmmn_ru_mil_inst`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `TIME_STAMP_` datetime(3) NULL DEFAULT NULL,
  `CASE_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `CASE_DEF_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `ELEMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_MIL_CASE_DEF`(`CASE_DEF_ID_`) USING BTREE,
  INDEX `ACT_IDX_MIL_CASE_INST`(`CASE_INST_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_MIL_CASE_DEF` FOREIGN KEY (`CASE_DEF_ID_`) REFERENCES `act_cmmn_casedef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_MIL_CASE_INST` FOREIGN KEY (`CASE_INST_ID_`) REFERENCES `act_cmmn_ru_case_inst` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_ru_mil_inst
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_ru_plan_item_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_ru_plan_item_inst`;
CREATE TABLE `act_cmmn_ru_plan_item_inst`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REV_` int(11) NOT NULL,
  `CASE_DEF_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CASE_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `STAGE_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `IS_STAGE_` bit(1) NULL DEFAULT NULL,
  `ELEMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `STATE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
  `START_USER_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `REFERENCE_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `REFERENCE_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT '',
  `ITEM_DEFINITION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `ITEM_DEFINITION_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `IS_COMPLETEABLE_` bit(1) NULL DEFAULT NULL,
  `IS_COUNT_ENABLED_` bit(1) NULL DEFAULT NULL,
  `VAR_COUNT_` int(11) NULL DEFAULT NULL,
  `SENTRY_PART_INST_COUNT_` int(11) NULL DEFAULT NULL,
  `LAST_AVAILABLE_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_ENABLED_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_DISABLED_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_STARTED_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_SUSPENDED_TIME_` datetime(3) NULL DEFAULT NULL,
  `COMPLETED_TIME_` datetime(3) NULL DEFAULT NULL,
  `OCCURRED_TIME_` datetime(3) NULL DEFAULT NULL,
  `TERMINATED_TIME_` datetime(3) NULL DEFAULT NULL,
  `EXIT_TIME_` datetime(3) NULL DEFAULT NULL,
  `ENDED_TIME_` datetime(3) NULL DEFAULT NULL,
  `ENTRY_CRITERION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `EXIT_CRITERION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_PLAN_ITEM_CASE_DEF`(`CASE_DEF_ID_`) USING BTREE,
  INDEX `ACT_IDX_PLAN_ITEM_CASE_INST`(`CASE_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_PLAN_ITEM_STAGE_INST`(`STAGE_INST_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_PLAN_ITEM_CASE_DEF` FOREIGN KEY (`CASE_DEF_ID_`) REFERENCES `act_cmmn_casedef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_PLAN_ITEM_CASE_INST` FOREIGN KEY (`CASE_INST_ID_`) REFERENCES `act_cmmn_ru_case_inst` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_ru_plan_item_inst
-- ----------------------------

-- ----------------------------
-- Table structure for act_cmmn_ru_sentry_part_inst
-- ----------------------------
DROP TABLE IF EXISTS `act_cmmn_ru_sentry_part_inst`;
CREATE TABLE `act_cmmn_ru_sentry_part_inst`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `REV_` int(11) NOT NULL,
  `CASE_DEF_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CASE_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `PLAN_ITEM_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `ON_PART_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `IF_PART_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TIME_STAMP_` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_SENTRY_CASE_DEF`(`CASE_DEF_ID_`) USING BTREE,
  INDEX `ACT_IDX_SENTRY_CASE_INST`(`CASE_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_SENTRY_PLAN_ITEM`(`PLAN_ITEM_INST_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_SENTRY_CASE_DEF` FOREIGN KEY (`CASE_DEF_ID_`) REFERENCES `act_cmmn_casedef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_SENTRY_CASE_INST` FOREIGN KEY (`CASE_INST_ID_`) REFERENCES `act_cmmn_ru_case_inst` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_SENTRY_PLAN_ITEM` FOREIGN KEY (`PLAN_ITEM_INST_ID_`) REFERENCES `act_cmmn_ru_plan_item_inst` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_cmmn_ru_sentry_part_inst
-- ----------------------------

-- ----------------------------
-- Table structure for act_co_content_item
-- ----------------------------
DROP TABLE IF EXISTS `act_co_content_item`;
CREATE TABLE `act_co_content_item`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `MIME_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TASK_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTENT_STORE_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTENT_STORE_NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `FIELD_` varchar(400) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTENT_AVAILABLE_` bit(1) NULL DEFAULT b'0',
  `CREATED_` timestamp(6) NULL DEFAULT NULL,
  `CREATED_BY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LAST_MODIFIED_` timestamp(6) NULL DEFAULT NULL,
  `LAST_MODIFIED_BY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTENT_SIZE_` bigint(20) NULL DEFAULT 0,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `idx_contitem_taskid`(`TASK_ID_`) USING BTREE,
  INDEX `idx_contitem_procid`(`PROC_INST_ID_`) USING BTREE,
  INDEX `idx_contitem_scope`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_co_content_item
-- ----------------------------

-- ----------------------------
-- Table structure for act_co_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_co_databasechangelog`;
CREATE TABLE `act_co_databasechangelog`  (
  `ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_co_databasechangelog
-- ----------------------------
INSERT INTO `act_co_databasechangelog` VALUES ('1', 'activiti', 'org/flowable/content/db/liquibase/flowable-content-db-changelog.xml', '2022-12-09 07:31:34', 1, 'EXECUTED', '8:7644d7165cfe799200a2abdd3419e8b6', 'createTable tableName=ACT_CO_CONTENT_ITEM; createIndex indexName=idx_contitem_taskid, tableName=ACT_CO_CONTENT_ITEM; createIndex indexName=idx_contitem_procid, tableName=ACT_CO_CONTENT_ITEM', '', NULL, '3.8.9', NULL, NULL, '0571128385');
INSERT INTO `act_co_databasechangelog` VALUES ('2', 'flowable', 'org/flowable/content/db/liquibase/flowable-content-db-changelog.xml', '2022-12-09 07:31:34', 2, 'EXECUTED', '8:fe7b11ac7dbbf9c43006b23bbab60bab', 'addColumn tableName=ACT_CO_CONTENT_ITEM; createIndex indexName=idx_contitem_scope, tableName=ACT_CO_CONTENT_ITEM', '', NULL, '3.8.9', NULL, NULL, '0571128385');

-- ----------------------------
-- Table structure for act_co_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_co_databasechangeloglock`;
CREATE TABLE `act_co_databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_co_databasechangeloglock
-- ----------------------------
INSERT INTO `act_co_databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for act_de_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_de_databasechangelog`;
CREATE TABLE `act_de_databasechangelog`  (
  `ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_de_databasechangelog
-- ----------------------------
INSERT INTO `act_de_databasechangelog` VALUES ('1', 'flowable', 'META-INF/liquibase/flowable-modeler-app-db-changelog.xml', '2022-12-09 07:32:11', 1, 'EXECUTED', '8:e70d1d9d3899a734296b2514ccc71501', 'createTable tableName=ACT_DE_MODEL; createIndex indexName=idx_proc_mod_created, tableName=ACT_DE_MODEL; createTable tableName=ACT_DE_MODEL_HISTORY; createIndex indexName=idx_proc_mod_history_proc, tableName=ACT_DE_MODEL_HISTORY; createTable tableN...', '', NULL, '3.8.9', NULL, NULL, '0571164806');
INSERT INTO `act_de_databasechangelog` VALUES ('3', 'flowable', 'META-INF/liquibase/flowable-modeler-app-db-changelog.xml', '2022-12-09 07:32:12', 2, 'EXECUTED', '8:3a9143bef2e45f2316231cc1369138b6', 'addColumn tableName=ACT_DE_MODEL; addColumn tableName=ACT_DE_MODEL_HISTORY', '', NULL, '3.8.9', NULL, NULL, '0571164806');

-- ----------------------------
-- Table structure for act_de_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_de_databasechangeloglock`;
CREATE TABLE `act_de_databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_de_databasechangeloglock
-- ----------------------------
INSERT INTO `act_de_databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for act_de_model
-- ----------------------------
DROP TABLE IF EXISTS `act_de_model`;
CREATE TABLE `act_de_model`  (
  `id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `name` varchar(400) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `model_key` varchar(400) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `description` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `model_comment` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `created` datetime(6) NULL DEFAULT NULL,
  `created_by` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `last_updated` datetime(6) NULL DEFAULT NULL,
  `last_updated_by` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `version` int(11) NULL DEFAULT NULL,
  `model_editor_json` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  `thumbnail` longblob NULL,
  `model_type` int(11) NULL DEFAULT NULL,
  `tenant_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_proc_mod_created`(`created_by`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_de_model
-- ----------------------------

-- ----------------------------
-- Table structure for act_de_model_history
-- ----------------------------
DROP TABLE IF EXISTS `act_de_model_history`;
CREATE TABLE `act_de_model_history`  (
  `id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `name` varchar(400) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `model_key` varchar(400) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `description` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `model_comment` varchar(4000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `created` datetime(6) NULL DEFAULT NULL,
  `created_by` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `last_updated` datetime(6) NULL DEFAULT NULL,
  `last_updated_by` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `removal_date` datetime(6) NULL DEFAULT NULL,
  `version` int(11) NULL DEFAULT NULL,
  `model_editor_json` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  `model_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `model_type` int(11) NULL DEFAULT NULL,
  `tenant_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_proc_mod_history_proc`(`model_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_de_model_history
-- ----------------------------

-- ----------------------------
-- Table structure for act_de_model_relation
-- ----------------------------
DROP TABLE IF EXISTS `act_de_model_relation`;
CREATE TABLE `act_de_model_relation`  (
  `id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `parent_model_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `model_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `relation_type` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_relation_parent`(`parent_model_id`) USING BTREE,
  INDEX `fk_relation_child`(`model_id`) USING BTREE,
  CONSTRAINT `fk_relation_child` FOREIGN KEY (`model_id`) REFERENCES `act_de_model` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_relation_parent` FOREIGN KEY (`parent_model_id`) REFERENCES `act_de_model` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_de_model_relation
-- ----------------------------

-- ----------------------------
-- Table structure for act_dmn_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_databasechangelog`;
CREATE TABLE `act_dmn_databasechangelog`  (
  `ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_dmn_databasechangelog
-- ----------------------------
INSERT INTO `act_dmn_databasechangelog` VALUES ('1', 'activiti', 'org/flowable/dmn/db/liquibase/flowable-dmn-db-changelog.xml', '2022-12-09 07:31:22', 1, 'EXECUTED', '8:c8701f1c71018b55029f450b2e9a10a1', 'createTable tableName=ACT_DMN_DEPLOYMENT; createTable tableName=ACT_DMN_DEPLOYMENT_RESOURCE; createTable tableName=ACT_DMN_DECISION_TABLE', '', NULL, '3.8.9', NULL, NULL, '0571116234');
INSERT INTO `act_dmn_databasechangelog` VALUES ('2', 'flowable', 'org/flowable/dmn/db/liquibase/flowable-dmn-db-changelog.xml', '2022-12-09 07:31:22', 2, 'EXECUTED', '8:47f94b27feb7df8a30d4e338c7bd5fb8', 'createTable tableName=ACT_DMN_HI_DECISION_EXECUTION', '', NULL, '3.8.9', NULL, NULL, '0571116234');
INSERT INTO `act_dmn_databasechangelog` VALUES ('3', 'flowable', 'org/flowable/dmn/db/liquibase/flowable-dmn-db-changelog.xml', '2022-12-09 07:31:23', 3, 'EXECUTED', '8:ac17eae89fbdccb6e08daf3c7797b579', 'addColumn tableName=ACT_DMN_HI_DECISION_EXECUTION', '', NULL, '3.8.9', NULL, NULL, '0571116234');
INSERT INTO `act_dmn_databasechangelog` VALUES ('4', 'flowable', 'org/flowable/dmn/db/liquibase/flowable-dmn-db-changelog.xml', '2022-12-09 07:31:23', 4, 'EXECUTED', '8:f73aabc4529e7292c2942073d1cff6f9', 'dropColumn columnName=PARENT_DEPLOYMENT_ID_, tableName=ACT_DMN_DECISION_TABLE', '', NULL, '3.8.9', NULL, NULL, '0571116234');
INSERT INTO `act_dmn_databasechangelog` VALUES ('5', 'flowable', 'org/flowable/dmn/db/liquibase/flowable-dmn-db-changelog.xml', '2022-12-09 07:31:24', 5, 'EXECUTED', '8:3e03528582dd4eeb4eb41f9b9539140d', 'modifyDataType columnName=DEPLOY_TIME_, tableName=ACT_DMN_DEPLOYMENT; modifyDataType columnName=START_TIME_, tableName=ACT_DMN_HI_DECISION_EXECUTION; modifyDataType columnName=END_TIME_, tableName=ACT_DMN_HI_DECISION_EXECUTION', '', NULL, '3.8.9', NULL, NULL, '0571116234');
INSERT INTO `act_dmn_databasechangelog` VALUES ('6', 'flowable', 'org/flowable/dmn/db/liquibase/flowable-dmn-db-changelog.xml', '2022-12-09 07:31:25', 6, 'EXECUTED', '8:646c6a061e0b6e8a62e69844ff96abb0', 'createIndex indexName=ACT_IDX_DEC_TBL_UNIQ, tableName=ACT_DMN_DECISION_TABLE', '', NULL, '3.8.9', NULL, NULL, '0571116234');

-- ----------------------------
-- Table structure for act_dmn_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_databasechangeloglock`;
CREATE TABLE `act_dmn_databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_dmn_databasechangeloglock
-- ----------------------------
INSERT INTO `act_dmn_databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for act_dmn_decision_table
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_decision_table`;
CREATE TABLE `act_dmn_decision_table`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `VERSION_` int(11) NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `RESOURCE_NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `ACT_IDX_DEC_TBL_UNIQ`(`KEY_`, `VERSION_`, `TENANT_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_dmn_decision_table
-- ----------------------------

-- ----------------------------
-- Table structure for act_dmn_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_deployment`;
CREATE TABLE `act_dmn_deployment`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOY_TIME_` datetime(3) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `PARENT_DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_dmn_deployment
-- ----------------------------

-- ----------------------------
-- Table structure for act_dmn_deployment_resource
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_deployment_resource`;
CREATE TABLE `act_dmn_deployment_resource`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `RESOURCE_BYTES_` longblob NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_dmn_deployment_resource
-- ----------------------------

-- ----------------------------
-- Table structure for act_dmn_hi_decision_execution
-- ----------------------------
DROP TABLE IF EXISTS `act_dmn_hi_decision_execution`;
CREATE TABLE `act_dmn_hi_decision_execution`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DECISION_DEFINITION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NULL DEFAULT NULL,
  `END_TIME_` datetime(3) NULL DEFAULT NULL,
  `INSTANCE_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `ACTIVITY_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `FAILED_` bit(1) NULL DEFAULT b'0',
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `EXECUTION_JSON_` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_dmn_hi_decision_execution
-- ----------------------------

-- ----------------------------
-- Table structure for act_evt_log
-- ----------------------------
DROP TABLE IF EXISTS `act_evt_log`;
CREATE TABLE `act_evt_log`  (
  `LOG_NR_` bigint(20) NOT NULL AUTO_INCREMENT,
  `TYPE_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TIME_STAMP_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DATA_` longblob NULL,
  `LOCK_OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  `IS_PROCESSED_` tinyint(4) NULL DEFAULT 0,
  PRIMARY KEY (`LOG_NR_`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_evt_log
-- ----------------------------

-- ----------------------------
-- Table structure for act_fo_databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_databasechangelog`;
CREATE TABLE `act_fo_databasechangelog`  (
  `ID` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_fo_databasechangelog
-- ----------------------------
INSERT INTO `act_fo_databasechangelog` VALUES ('1', 'activiti', 'org/flowable/form/db/liquibase/flowable-form-db-changelog.xml', '2022-12-09 07:31:28', 1, 'EXECUTED', '8:033ebf9380889aed7c453927ecc3250d', 'createTable tableName=ACT_FO_FORM_DEPLOYMENT; createTable tableName=ACT_FO_FORM_RESOURCE; createTable tableName=ACT_FO_FORM_DEFINITION; createTable tableName=ACT_FO_FORM_INSTANCE', '', NULL, '3.8.9', NULL, NULL, '0571122638');
INSERT INTO `act_fo_databasechangelog` VALUES ('2', 'flowable', 'org/flowable/form/db/liquibase/flowable-form-db-changelog.xml', '2022-12-09 07:31:29', 2, 'EXECUTED', '8:986365ceb40445ce3b27a8e6b40f159b', 'addColumn tableName=ACT_FO_FORM_INSTANCE', '', NULL, '3.8.9', NULL, NULL, '0571122638');
INSERT INTO `act_fo_databasechangelog` VALUES ('3', 'flowable', 'org/flowable/form/db/liquibase/flowable-form-db-changelog.xml', '2022-12-09 07:31:29', 3, 'EXECUTED', '8:abf482518ceb09830ef674e52c06bf15', 'dropColumn columnName=PARENT_DEPLOYMENT_ID_, tableName=ACT_FO_FORM_DEFINITION', '', NULL, '3.8.9', NULL, NULL, '0571122638');
INSERT INTO `act_fo_databasechangelog` VALUES ('4', 'flowable', 'org/flowable/form/db/liquibase/flowable-form-db-changelog.xml', '2022-12-09 07:31:30', 4, 'EXECUTED', '8:2087829f22a4b2298dbf530681c74854', 'modifyDataType columnName=DEPLOY_TIME_, tableName=ACT_FO_FORM_DEPLOYMENT; modifyDataType columnName=SUBMITTED_DATE_, tableName=ACT_FO_FORM_INSTANCE', '', NULL, '3.8.9', NULL, NULL, '0571122638');
INSERT INTO `act_fo_databasechangelog` VALUES ('5', 'flowable', 'org/flowable/form/db/liquibase/flowable-form-db-changelog.xml', '2022-12-09 07:31:31', 5, 'EXECUTED', '8:b4be732b89e5ca028bdd520c6ad4d446', 'createIndex indexName=ACT_IDX_FORM_DEF_UNIQ, tableName=ACT_FO_FORM_DEFINITION', '', NULL, '3.8.9', NULL, NULL, '0571122638');

-- ----------------------------
-- Table structure for act_fo_databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_databasechangeloglock`;
CREATE TABLE `act_fo_databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_fo_databasechangeloglock
-- ----------------------------
INSERT INTO `act_fo_databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for act_fo_form_definition
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_form_definition`;
CREATE TABLE `act_fo_form_definition`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `VERSION_` int(11) NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `RESOURCE_NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `ACT_IDX_FORM_DEF_UNIQ`(`KEY_`, `VERSION_`, `TENANT_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_fo_form_definition
-- ----------------------------

-- ----------------------------
-- Table structure for act_fo_form_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_form_deployment`;
CREATE TABLE `act_fo_form_deployment`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOY_TIME_` datetime(3) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `PARENT_DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_fo_form_deployment
-- ----------------------------

-- ----------------------------
-- Table structure for act_fo_form_instance
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_form_instance`;
CREATE TABLE `act_fo_form_instance`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `FORM_DEFINITION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `TASK_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `SUBMITTED_DATE_` datetime(3) NULL DEFAULT NULL,
  `SUBMITTED_BY_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `FORM_VALUES_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_fo_form_instance
-- ----------------------------

-- ----------------------------
-- Table structure for act_fo_form_resource
-- ----------------------------
DROP TABLE IF EXISTS `act_fo_form_resource`;
CREATE TABLE `act_fo_form_resource`  (
  `ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `NAME_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `RESOURCE_BYTES_` longblob NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_fo_form_resource
-- ----------------------------

-- ----------------------------
-- Table structure for act_ge_bytearray
-- ----------------------------
DROP TABLE IF EXISTS `act_ge_bytearray`;
CREATE TABLE `act_ge_bytearray`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BYTES_` longblob NULL,
  `GENERATED_` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_FK_BYTEARR_DEPL`(`DEPLOYMENT_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_BYTEARR_DEPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ge_bytearray
-- ----------------------------

-- ----------------------------
-- Table structure for act_ge_property
-- ----------------------------
DROP TABLE IF EXISTS `act_ge_property`;
CREATE TABLE `act_ge_property`  (
  `NAME_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VALUE_` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`NAME_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ge_property
-- ----------------------------
INSERT INTO `act_ge_property` VALUES ('cfg.execution-related-entities-count', 'true', 1);
INSERT INTO `act_ge_property` VALUES ('cfg.task-related-entities-count', 'true', 1);
INSERT INTO `act_ge_property` VALUES ('common.schema.version', '6.5.0.1', 1);
INSERT INTO `act_ge_property` VALUES ('entitylink.schema.version', '6.5.0.1', 1);
INSERT INTO `act_ge_property` VALUES ('eventsubscription.schema.version', '6.5.0.1', 1);
INSERT INTO `act_ge_property` VALUES ('identitylink.schema.version', '6.5.0.1', 1);
INSERT INTO `act_ge_property` VALUES ('job.schema.version', '6.5.0.1', 1);
INSERT INTO `act_ge_property` VALUES ('next.dbid', '1', 1);
INSERT INTO `act_ge_property` VALUES ('schema.history', 'create(6.5.0.1)', 1);
INSERT INTO `act_ge_property` VALUES ('schema.version', '6.5.0.1', 1);
INSERT INTO `act_ge_property` VALUES ('task.schema.version', '6.5.0.1', 1);
INSERT INTO `act_ge_property` VALUES ('variable.schema.version', '6.5.0.1', 1);

-- ----------------------------
-- Table structure for act_hi_actinst
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_actinst`;
CREATE TABLE `act_hi_actinst`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT 1,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CALL_PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ASSIGNEE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) NULL DEFAULT NULL,
  `DURATION_` bigint(20) NULL DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_ACT_INST_START`(`START_TIME_`) USING BTREE,
  INDEX `ACT_IDX_HI_ACT_INST_END`(`END_TIME_`) USING BTREE,
  INDEX `ACT_IDX_HI_ACT_INST_PROCINST`(`PROC_INST_ID_`, `ACT_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_ACT_INST_EXEC`(`EXECUTION_ID_`, `ACT_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_actinst
-- ----------------------------

-- ----------------------------
-- Table structure for act_hi_attachment
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_attachment`;
CREATE TABLE `act_hi_attachment`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `URL_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CONTENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TIME_` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_attachment
-- ----------------------------

-- ----------------------------
-- Table structure for act_hi_comment
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_comment`;
CREATE TABLE `act_hi_comment`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACTION_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `MESSAGE_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `FULL_MSG_` longblob NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_comment
-- ----------------------------

-- ----------------------------
-- Table structure for act_hi_detail
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_detail`;
CREATE TABLE `act_hi_detail`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `BYTEARRAY_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DOUBLE_` double NULL DEFAULT NULL,
  `LONG_` bigint(20) NULL DEFAULT NULL,
  `TEXT_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TEXT2_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_PROC_INST`(`PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_ACT_INST`(`ACT_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_TIME`(`TIME_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_NAME`(`NAME_`) USING BTREE,
  INDEX `ACT_IDX_HI_DETAIL_TASK_ID`(`TASK_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_detail
-- ----------------------------

-- ----------------------------
-- Table structure for act_hi_entitylink
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_entitylink`;
CREATE TABLE `act_hi_entitylink`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `LINK_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REF_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REF_SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REF_SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HIERARCHY_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_ENT_LNK_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`, `LINK_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_ENT_LNK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`, `LINK_TYPE_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_entitylink
-- ----------------------------

-- ----------------------------
-- Table structure for act_hi_identitylink
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_identitylink`;
CREATE TABLE `act_hi_identitylink`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `GROUP_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_IDENT_LNK_USER`(`USER_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_IDENT_LNK_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_IDENT_LNK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_IDENT_LNK_TASK`(`TASK_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_IDENT_LNK_PROCINST`(`PROC_INST_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_identitylink
-- ----------------------------

-- ----------------------------
-- Table structure for act_hi_procinst
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_procinst`;
CREATE TABLE `act_hi_procinst`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT 1,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `BUSINESS_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) NULL DEFAULT NULL,
  `DURATION_` bigint(20) NULL DEFAULT NULL,
  `START_USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `END_ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUPER_PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CALLBACK_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CALLBACK_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `PROC_INST_ID_`(`PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_PRO_INST_END`(`END_TIME_`) USING BTREE,
  INDEX `ACT_IDX_HI_PRO_I_BUSKEY`(`BUSINESS_KEY_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_procinst
-- ----------------------------

-- ----------------------------
-- Table structure for act_hi_taskinst
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_taskinst`;
CREATE TABLE `act_hi_taskinst`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT 1,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ASSIGNEE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `CLAIM_TIME_` datetime(3) NULL DEFAULT NULL,
  `END_TIME_` datetime(3) NULL DEFAULT NULL,
  `DURATION_` bigint(20) NULL DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PRIORITY_` int(11) NULL DEFAULT NULL,
  `DUE_DATE_` datetime(3) NULL DEFAULT NULL,
  `FORM_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `LAST_UPDATED_TIME_` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_TASK_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_TASK_SUB_SCOPE`(`SUB_SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_TASK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_TASK_INST_PROCINST`(`PROC_INST_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_taskinst
-- ----------------------------

-- ----------------------------
-- Table structure for act_hi_tsk_log
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_tsk_log`;
CREATE TABLE `act_hi_tsk_log`  (
  `ID_` bigint(20) NOT NULL AUTO_INCREMENT,
  `TYPE_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TIME_STAMP_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DATA_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_tsk_log
-- ----------------------------

-- ----------------------------
-- Table structure for act_hi_varinst
-- ----------------------------
DROP TABLE IF EXISTS `act_hi_varinst`;
CREATE TABLE `act_hi_varinst`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT 1,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DOUBLE_` double NULL DEFAULT NULL,
  `LONG_` bigint(20) NULL DEFAULT NULL,
  `TEXT_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TEXT2_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
  `LAST_UPDATED_TIME_` datetime(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_PROCVAR_NAME_TYPE`(`NAME_`, `VAR_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_VAR_SCOPE_ID_TYPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_VAR_SUB_ID_TYPE`(`SUB_SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_HI_PROCVAR_PROC_INST`(`PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_PROCVAR_TASK_ID`(`TASK_ID_`) USING BTREE,
  INDEX `ACT_IDX_HI_PROCVAR_EXE`(`EXECUTION_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_hi_varinst
-- ----------------------------

-- ----------------------------
-- Table structure for act_id_bytearray
-- ----------------------------
DROP TABLE IF EXISTS `act_id_bytearray`;
CREATE TABLE `act_id_bytearray`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BYTES_` longblob NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_id_bytearray
-- ----------------------------

-- ----------------------------
-- Table structure for act_id_info
-- ----------------------------
DROP TABLE IF EXISTS `act_id_info`;
CREATE TABLE `act_id_info`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `USER_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TYPE_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `VALUE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PASSWORD_` longblob NULL,
  `PARENT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_id_info
-- ----------------------------

-- ----------------------------
-- Table structure for act_id_priv
-- ----------------------------
DROP TABLE IF EXISTS `act_id_priv`;
CREATE TABLE `act_id_priv`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `ACT_UNIQ_PRIV_NAME`(`NAME_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_id_priv
-- ----------------------------

-- ----------------------------
-- Table structure for act_id_priv_mapping
-- ----------------------------
DROP TABLE IF EXISTS `act_id_priv_mapping`;
CREATE TABLE `act_id_priv_mapping`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PRIV_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `GROUP_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_FK_PRIV_MAPPING`(`PRIV_ID_`) USING BTREE,
  INDEX `ACT_IDX_PRIV_USER`(`USER_ID_`) USING BTREE,
  INDEX `ACT_IDX_PRIV_GROUP`(`GROUP_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_PRIV_MAPPING` FOREIGN KEY (`PRIV_ID_`) REFERENCES `act_id_priv` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_id_priv_mapping
-- ----------------------------

-- ----------------------------
-- Table structure for act_id_property
-- ----------------------------
DROP TABLE IF EXISTS `act_id_property`;
CREATE TABLE `act_id_property`  (
  `NAME_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VALUE_` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`NAME_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_id_property
-- ----------------------------
INSERT INTO `act_id_property` VALUES ('schema.version', '6.5.0.1', 1);

-- ----------------------------
-- Table structure for act_id_token
-- ----------------------------
DROP TABLE IF EXISTS `act_id_token`;
CREATE TABLE `act_id_token`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TOKEN_VALUE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TOKEN_DATE_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `IP_ADDRESS_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `USER_AGENT_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TOKEN_DATA_` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_id_token
-- ----------------------------

-- ----------------------------
-- Table structure for act_procdef_info
-- ----------------------------
DROP TABLE IF EXISTS `act_procdef_info`;
CREATE TABLE `act_procdef_info`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `INFO_JSON_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `ACT_UNIQ_INFO_PROCDEF`(`PROC_DEF_ID_`) USING BTREE,
  INDEX `ACT_IDX_INFO_PROCDEF`(`PROC_DEF_ID_`) USING BTREE,
  INDEX `ACT_FK_INFO_JSON_BA`(`INFO_JSON_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_INFO_JSON_BA` FOREIGN KEY (`INFO_JSON_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_INFO_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_procdef_info
-- ----------------------------

-- ----------------------------
-- Table structure for act_re_deployment
-- ----------------------------
DROP TABLE IF EXISTS `act_re_deployment`;
CREATE TABLE `act_re_deployment`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `DEPLOY_TIME_` timestamp(3) NULL DEFAULT NULL,
  `DERIVED_FROM_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DERIVED_FROM_ROOT_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PARENT_DEPLOYMENT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ENGINE_VERSION_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_re_deployment
-- ----------------------------

-- ----------------------------
-- Table structure for act_re_model
-- ----------------------------
DROP TABLE IF EXISTS `act_re_model`;
CREATE TABLE `act_re_model`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LAST_UPDATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `VERSION_` int(11) NULL DEFAULT NULL,
  `META_INFO_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EDITOR_SOURCE_VALUE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_FK_MODEL_SOURCE`(`EDITOR_SOURCE_VALUE_ID_`) USING BTREE,
  INDEX `ACT_FK_MODEL_SOURCE_EXTRA`(`EDITOR_SOURCE_EXTRA_VALUE_ID_`) USING BTREE,
  INDEX `ACT_FK_MODEL_DEPLOYMENT`(`DEPLOYMENT_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_MODEL_DEPLOYMENT` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_MODEL_SOURCE` FOREIGN KEY (`EDITOR_SOURCE_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_MODEL_SOURCE_EXTRA` FOREIGN KEY (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_re_model
-- ----------------------------

-- ----------------------------
-- Table structure for act_re_procdef
-- ----------------------------
DROP TABLE IF EXISTS `act_re_procdef`;
CREATE TABLE `act_re_procdef`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `DEPLOYMENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `RESOURCE_NAME_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DGRM_RESOURCE_NAME_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HAS_START_FORM_KEY_` tinyint(4) NULL DEFAULT NULL,
  `HAS_GRAPHICAL_NOTATION_` tinyint(4) NULL DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `ENGINE_VERSION_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DERIVED_FROM_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DERIVED_FROM_ROOT_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DERIVED_VERSION_` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE INDEX `ACT_UNIQ_PROCDEF`(`KEY_`, `VERSION_`, `DERIVED_VERSION_`, `TENANT_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_re_procdef
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_actinst
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_actinst`;
CREATE TABLE `act_ru_actinst`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT 1,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CALL_PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ASSIGNEE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) NULL DEFAULT NULL,
  `DURATION_` bigint(20) NULL DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_RU_ACTI_START`(`START_TIME_`) USING BTREE,
  INDEX `ACT_IDX_RU_ACTI_END`(`END_TIME_`) USING BTREE,
  INDEX `ACT_IDX_RU_ACTI_PROC`(`PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_IDX_RU_ACTI_PROC_ACT`(`PROC_INST_ID_`, `ACT_ID_`) USING BTREE,
  INDEX `ACT_IDX_RU_ACTI_EXEC`(`EXECUTION_ID_`) USING BTREE,
  INDEX `ACT_IDX_RU_ACTI_EXEC_ACT`(`EXECUTION_ID_`, `ACT_ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_actinst
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_deadletter_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_deadletter_job`;
CREATE TABLE `act_ru_deadletter_job`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ELEMENT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ELEMENT_NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_DEADLETTER_JOB_EXCEPTION_STACK_ID`(`EXCEPTION_STACK_ID_`) USING BTREE,
  INDEX `ACT_IDX_DEADLETTER_JOB_CUSTOM_VALUES_ID`(`CUSTOM_VALUES_ID_`) USING BTREE,
  INDEX `ACT_IDX_DJOB_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_DJOB_SUB_SCOPE`(`SUB_SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_DJOB_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_FK_DEADLETTER_JOB_EXECUTION`(`EXECUTION_ID_`) USING BTREE,
  INDEX `ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE`(`PROCESS_INSTANCE_ID_`) USING BTREE,
  INDEX `ACT_FK_DEADLETTER_JOB_PROC_DEF`(`PROC_DEF_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_DEADLETTER_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_DEADLETTER_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_DEADLETTER_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_DEADLETTER_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_deadletter_job
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_entitylink
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_entitylink`;
CREATE TABLE `act_ru_entitylink`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `CREATE_TIME_` datetime(3) NULL DEFAULT NULL,
  `LINK_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REF_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REF_SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REF_SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HIERARCHY_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_ENT_LNK_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`, `LINK_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_ENT_LNK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`, `LINK_TYPE_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_entitylink
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_event_subscr
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_event_subscr`;
CREATE TABLE `act_ru_event_subscr`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `EVENT_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EVENT_NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACTIVITY_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CONFIGURATION_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATED_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_EVENT_SUBSCR_CONFIG_`(`CONFIGURATION_`) USING BTREE,
  INDEX `ACT_FK_EVENT_EXEC`(`EXECUTION_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_EVENT_EXEC` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_event_subscr
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_execution
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_execution`;
CREATE TABLE `act_ru_execution`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BUSINESS_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PARENT_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUPER_EXEC_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ROOT_PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `IS_ACTIVE_` tinyint(4) NULL DEFAULT NULL,
  `IS_CONCURRENT_` tinyint(4) NULL DEFAULT NULL,
  `IS_SCOPE_` tinyint(4) NULL DEFAULT NULL,
  `IS_EVENT_SCOPE_` tinyint(4) NULL DEFAULT NULL,
  `IS_MI_ROOT_` tinyint(4) NULL DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) NULL DEFAULT NULL,
  `CACHED_ENT_STATE_` int(11) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_ACT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `START_TIME_` datetime(3) NULL DEFAULT NULL,
  `START_USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  `IS_COUNT_ENABLED_` tinyint(4) NULL DEFAULT NULL,
  `EVT_SUBSCR_COUNT_` int(11) NULL DEFAULT NULL,
  `TASK_COUNT_` int(11) NULL DEFAULT NULL,
  `JOB_COUNT_` int(11) NULL DEFAULT NULL,
  `TIMER_JOB_COUNT_` int(11) NULL DEFAULT NULL,
  `SUSP_JOB_COUNT_` int(11) NULL DEFAULT NULL,
  `DEADLETTER_JOB_COUNT_` int(11) NULL DEFAULT NULL,
  `VAR_COUNT_` int(11) NULL DEFAULT NULL,
  `ID_LINK_COUNT_` int(11) NULL DEFAULT NULL,
  `CALLBACK_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CALLBACK_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_EXEC_BUSKEY`(`BUSINESS_KEY_`) USING BTREE,
  INDEX `ACT_IDC_EXEC_ROOT`(`ROOT_PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_FK_EXE_PROCINST`(`PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_FK_EXE_PARENT`(`PARENT_ID_`) USING BTREE,
  INDEX `ACT_FK_EXE_SUPER`(`SUPER_EXEC_`) USING BTREE,
  INDEX `ACT_FK_EXE_PROCDEF`(`PROC_DEF_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_EXE_PARENT` FOREIGN KEY (`PARENT_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_EXE_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_EXE_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ACT_FK_EXE_SUPER` FOREIGN KEY (`SUPER_EXEC_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_execution
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_history_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_history_job`;
CREATE TABLE `act_ru_history_job`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LOCK_OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `RETRIES_` int(11) NULL DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ADV_HANDLER_CFG_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_history_job
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_identitylink
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_identitylink`;
CREATE TABLE `act_ru_identitylink`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `GROUP_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `USER_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_IDENT_LNK_USER`(`USER_ID_`) USING BTREE,
  INDEX `ACT_IDX_IDENT_LNK_GROUP`(`GROUP_ID_`) USING BTREE,
  INDEX `ACT_IDX_IDENT_LNK_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_IDENT_LNK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_ATHRZ_PROCEDEF`(`PROC_DEF_ID_`) USING BTREE,
  INDEX `ACT_FK_TSKASS_TASK`(`TASK_ID_`) USING BTREE,
  INDEX `ACT_FK_IDL_PROCINST`(`PROC_INST_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_ATHRZ_PROCEDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_IDL_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_TSKASS_TASK` FOREIGN KEY (`TASK_ID_`) REFERENCES `act_ru_task` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_identitylink
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_job`;
CREATE TABLE `act_ru_job`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LOCK_OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ELEMENT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ELEMENT_NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `RETRIES_` int(11) NULL DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_JOB_EXCEPTION_STACK_ID`(`EXCEPTION_STACK_ID_`) USING BTREE,
  INDEX `ACT_IDX_JOB_CUSTOM_VALUES_ID`(`CUSTOM_VALUES_ID_`) USING BTREE,
  INDEX `ACT_IDX_JOB_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_JOB_SUB_SCOPE`(`SUB_SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_JOB_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_FK_JOB_EXECUTION`(`EXECUTION_ID_`) USING BTREE,
  INDEX `ACT_FK_JOB_PROCESS_INSTANCE`(`PROCESS_INSTANCE_ID_`) USING BTREE,
  INDEX `ACT_FK_JOB_PROC_DEF`(`PROC_DEF_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_job
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_suspended_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_suspended_job`;
CREATE TABLE `act_ru_suspended_job`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ELEMENT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ELEMENT_NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `RETRIES_` int(11) NULL DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_SUSPENDED_JOB_EXCEPTION_STACK_ID`(`EXCEPTION_STACK_ID_`) USING BTREE,
  INDEX `ACT_IDX_SUSPENDED_JOB_CUSTOM_VALUES_ID`(`CUSTOM_VALUES_ID_`) USING BTREE,
  INDEX `ACT_IDX_SJOB_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_SJOB_SUB_SCOPE`(`SUB_SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_SJOB_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_FK_SUSPENDED_JOB_EXECUTION`(`EXECUTION_ID_`) USING BTREE,
  INDEX `ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE`(`PROCESS_INSTANCE_ID_`) USING BTREE,
  INDEX `ACT_FK_SUSPENDED_JOB_PROC_DEF`(`PROC_DEF_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_SUSPENDED_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_SUSPENDED_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_SUSPENDED_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_SUSPENDED_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_suspended_job
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_task
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_task`;
CREATE TABLE `act_ru_task`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ASSIGNEE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DELEGATION_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PRIORITY_` int(11) NULL DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `DUE_DATE_` datetime(3) NULL DEFAULT NULL,
  `CATEGORY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  `FORM_KEY_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CLAIM_TIME_` datetime(3) NULL DEFAULT NULL,
  `IS_COUNT_ENABLED_` tinyint(4) NULL DEFAULT NULL,
  `VAR_COUNT_` int(11) NULL DEFAULT NULL,
  `ID_LINK_COUNT_` int(11) NULL DEFAULT NULL,
  `SUB_TASK_COUNT_` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_TASK_CREATE`(`CREATE_TIME_`) USING BTREE,
  INDEX `ACT_IDX_TASK_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_TASK_SUB_SCOPE`(`SUB_SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_TASK_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_FK_TASK_EXE`(`EXECUTION_ID_`) USING BTREE,
  INDEX `ACT_FK_TASK_PROCINST`(`PROC_INST_ID_`) USING BTREE,
  INDEX `ACT_FK_TASK_PROCDEF`(`PROC_DEF_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_TASK_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_TASK_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_TASK_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_task
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_timer_job
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_timer_job`;
CREATE TABLE `act_ru_timer_job`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LOCK_OWNER_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCLUSIVE_` tinyint(1) NULL DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ELEMENT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ELEMENT_NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_DEFINITION_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `RETRIES_` int(11) NULL DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CUSTOM_VALUES_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `TENANT_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_TIMER_JOB_EXCEPTION_STACK_ID`(`EXCEPTION_STACK_ID_`) USING BTREE,
  INDEX `ACT_IDX_TIMER_JOB_CUSTOM_VALUES_ID`(`CUSTOM_VALUES_ID_`) USING BTREE,
  INDEX `ACT_IDX_TJOB_SCOPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_TJOB_SUB_SCOPE`(`SUB_SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_TJOB_SCOPE_DEF`(`SCOPE_DEFINITION_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_FK_TIMER_JOB_EXECUTION`(`EXECUTION_ID_`) USING BTREE,
  INDEX `ACT_FK_TIMER_JOB_PROCESS_INSTANCE`(`PROCESS_INSTANCE_ID_`) USING BTREE,
  INDEX `ACT_FK_TIMER_JOB_PROC_DEF`(`PROC_DEF_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_TIMER_JOB_CUSTOM_VALUES` FOREIGN KEY (`CUSTOM_VALUES_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_TIMER_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_TIMER_JOB_EXECUTION` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_TIMER_JOB_PROCESS_INSTANCE` FOREIGN KEY (`PROCESS_INSTANCE_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_TIMER_JOB_PROC_DEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_timer_job
-- ----------------------------

-- ----------------------------
-- Table structure for act_ru_variable
-- ----------------------------
DROP TABLE IF EXISTS `act_ru_variable`;
CREATE TABLE `act_ru_variable`  (
  `ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REV_` int(11) NULL DEFAULT NULL,
  `TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TASK_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `DOUBLE_` double NULL DEFAULT NULL,
  `LONG_` bigint(20) NULL DEFAULT NULL,
  `TEXT_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `TEXT2_` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  INDEX `ACT_IDX_RU_VAR_SCOPE_ID_TYPE`(`SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_IDX_RU_VAR_SUB_ID_TYPE`(`SUB_SCOPE_ID_`, `SCOPE_TYPE_`) USING BTREE,
  INDEX `ACT_FK_VAR_BYTEARRAY`(`BYTEARRAY_ID_`) USING BTREE,
  INDEX `ACT_IDX_VARIABLE_TASK_ID`(`TASK_ID_`) USING BTREE,
  INDEX `ACT_FK_VAR_EXE`(`EXECUTION_ID_`) USING BTREE,
  INDEX `ACT_FK_VAR_PROCINST`(`PROC_INST_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_VAR_BYTEARRAY` FOREIGN KEY (`BYTEARRAY_ID_`) REFERENCES `act_ge_bytearray` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_VAR_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ACT_FK_VAR_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ru_variable
-- ----------------------------

-- ----------------------------
-- Table structure for channelinfo
-- ----------------------------
DROP TABLE IF EXISTS `channelinfo`;
CREATE TABLE `channelinfo`  (
  `CHANNELID` bigint(20) NOT NULL COMMENT '主键id',
  `SITEID` bigint(20) NULL DEFAULT NULL COMMENT '站点id',
  `CHNLNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目名称',
  `CHNLDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `PARENTID` bigint(20) NULL DEFAULT 0 COMMENT '父栏目id',
  `CHNLORDER` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `CHNLDATAPATH` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目存放位置',
  `STATUS` int(5) NOT NULL DEFAULT 0 COMMENT '栏目状态，逻辑删除用(0:启用,1:停用)',
  `CHNLOUTLINETEMP` bigint(20) NULL DEFAULT NULL COMMENT '概览模板（模板表主键）',
  `CHNLDETAILTEMP` bigint(20) NULL DEFAULT NULL COMMENT '细览模板',
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除栏目的用户',
  `OPERTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '被删除的时间',
  `EDITPAGE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义编辑页面（默认：页面路径）',
  `LISTPAGE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义列表页面',
  `SHOWPAGE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义查看页面',
  `STATICFILE` int(2) NULL DEFAULT NULL COMMENT '生成静态文件（1：是，0：否）',
  `TENTTYPE` bigint(20) NULL DEFAULT NULL COMMENT '内容类型',
  `ELSES` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其他模板',
  `SAVERULES` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章页存储规则',
  `SITENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站点名',
  `SKUNUMBER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一标识',
  `REMARK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `TABLEID` bigint(20) NULL DEFAULT NULL COMMENT '元数据表标识',
  `CONDITION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目检索条件',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `LISTTPL` bigint(20) NULL DEFAULT NULL COMMENT '概览模板（模板表主键）',
  `DETAILTPL` bigint(20) NULL DEFAULT NULL COMMENT '详情模板',
  `EDITTPL` bigint(20) NULL DEFAULT NULL COMMENT '编辑页模板',
  `STOP` int(2) NULL DEFAULT NULL COMMENT '是否停用',
  `FILECOLUMN` bigint(20) NULL DEFAULT NULL,
  `IMGCOLUMN` bigint(20) NULL DEFAULT NULL,
  `VIDEOCOLUMN` bigint(20) NULL DEFAULT NULL,
  `RADIOCOLUMN` bigint(20) NULL DEFAULT NULL,
  `LISTURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `EDITURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DETAILURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORKFLOW` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `METADATATYPE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据类型',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `QUOTAID` bigint(20) NULL DEFAULT NULL COMMENT '引用栏目id',
  `QUOTANAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '引用栏目',
  `menuid` bigint(20) NULL DEFAULT NULL COMMENT '前台菜单id',
  `menuname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前台菜单名称',
  PRIMARY KEY (`CHANNELID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '栏目信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of channelinfo
-- ----------------------------
INSERT INTO `channelinfo` VALUES (1601101725998522370, 1601101087986163714, '样例图片栏目', NULL, 0, 0, '\\yltpzd\\yltplm', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'yltplm', NULL, NULL, NULL, 'admin', 1, '2022-12-09 14:29:33', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `channelinfo` VALUES (1601102031574540290, 1601101189513486337, '样例视频栏目', NULL, 0, 0, '', 0, NULL, NULL, NULL, '2022-12-09 06:30:33', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ylsplm', NULL, NULL, NULL, 'admin', 1, '2022-12-09 06:30:33', 1, '2022-12-09 14:31:08', NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL);
INSERT INTO `channelinfo` VALUES (1601102266648502274, 1601101295927173121, '样例文件栏目', NULL, 0, 0, '\\ylwjzd\\ylwjlm', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ylwjlm', NULL, NULL, NULL, 'admin', 1, '2022-12-09 14:31:42', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `channelinfo` VALUES (1601102364627443714, 1601101376998875137, '样例音频栏目', NULL, 0, 0, '\\ylypzd\\ylyplm', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ylyplm', NULL, NULL, NULL, 'admin', 1, '2022-12-09 14:32:05', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `channelinfo` VALUES (1601102588108349442, 1601100935430938625, '样例元数据栏目', NULL, 0, 0, '\\ylzd\\ylysjlm', 0, NULL, NULL, NULL, '2022-12-09 06:59:47', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ylysjlm', NULL, 1601107907144445953, NULL, 'admin', 1, '2022-12-09 06:59:47', 1, '2022-12-09 15:00:22', NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'table', 'admin', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `CLASSID` decimal(20, 0) NOT NULL,
  `CLASSNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSCODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PARENTID` decimal(20, 0) NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFYTIME` datetime NULL DEFAULT NULL,
  `MODIFYUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL,
  `SEQENCING` int(3) NULL DEFAULT NULL,
  `ICON_PATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ICON_URL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODULE_ID` bigint(20) NULL DEFAULT NULL,
  `MODULE_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BMNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MATCHG` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `METHODID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`CLASSID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1534445790586621954, '可视化管理', '可视化管理', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534446048481792001, '登录组件', '登录组件', NULL, 1534445790586621954, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534446398563569665, '登录类型', '登录类型', NULL, 1534446048481792001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534446498203455489, '账号密码', '账号密码', NULL, 1534446398563569665, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534446558928588802, '手机号验证码', '手机号验证码', NULL, 1534446398563569665, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534446630907039746, 'APP扫码', 'APP扫码', NULL, 1534446398563569665, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534446698083012610, '第三方登录', '第三方登录', NULL, 1534446048481792001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534446749610037250, '微信登录', '微信登录', NULL, 1534446698083012610, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534446809278205953, 'QQ登录', 'QQ登录', NULL, 1534446698083012610, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534446883714519042, '验证方式', '验证方式', NULL, 1534446048481792001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534447017248575489, '图形验证码', '图形验证码', NULL, 1534446883714519042, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534447170470694914, '滑动拼图验证码', '滑动拼图验证码', NULL, 1534446883714519042, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1534447257263427585, '文字点选验证码', '文字点选验证码', NULL, 1534446883714519042, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO `class` VALUES (1601108948321693697, '样例分类法', '样例分类法', NULL, 0, '2022-12-09 14:58:15', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 1601107447104794626, '样例元数据模块');
INSERT INTO `class` VALUES (1601109046279663617, '产品大小', '产品大小', NULL, 1601108948321693697, '2022-12-09 14:58:38', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 1601107447104794626, '样例元数据模块');
INSERT INTO `class` VALUES (1601109106891550721, '1-10', '1-10', NULL, 1601109046279663617, '2022-12-09 14:58:53', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, '');
INSERT INTO `class` VALUES (1601109136461393921, '10-100', '10-100', NULL, 1601109046279663617, '2022-12-09 14:59:00', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, '');
INSERT INTO `class` VALUES (1601109181009096706, '100-1000', '100-1000', NULL, 1601109046279663617, '2022-12-09 14:59:10', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, '');

-- ----------------------------
-- Table structure for companyinfo
-- ----------------------------
DROP TABLE IF EXISTS `companyinfo`;
CREATE TABLE `companyinfo`  (
  `COMPANYID` decimal(20, 0) NOT NULL COMMENT '主键ID',
  `CPYNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `CPYDESC` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公司简介',
  `REGTIME` datetime NULL DEFAULT NULL COMMENT '当前时间',
  `STATUS` decimal(5, 0) NULL DEFAULT 1 COMMENT '0未开通 1开通',
  `PRINCIPAL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `VALIDITY` decimal(5, 0) NULL DEFAULT NULL COMMENT '有效期',
  `ENABLE` decimal(5, 0) NULL DEFAULT NULL COMMENT '是 否  0,1',
  `BSNUM` decimal(5, 0) NULL DEFAULT NULL COMMENT '购买站点数',
  `PQPLUG` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '插件ID,格式如下：1,2,3  ',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  `CPYORDER` decimal(5, 0) NULL DEFAULT NULL COMMENT '排序',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `PARENT_ID` bigint(20) NULL DEFAULT NULL COMMENT '父机构id',
  `PARENT_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父机构',
  PRIMARY KEY (`COMPANYID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of companyinfo
-- ----------------------------
INSERT INTO `companyinfo` VALUES (1167323268476895233, '一体化', NULL, NULL, 1, '12', 0, 0, 0, NULL, 'admin', '2022-11-10 17:48:28', 1167324599977414658, '13213221222', 0, NULL, '2022-11-11 07:10:31', NULL, 0, '根机构');
INSERT INTO `companyinfo` VALUES (1549309528875601922, '样式风格--勿删', NULL, NULL, 1, '翟莹', 0, 0, 0, NULL, 'admin', '2022-07-19 16:25:51', 1167324599977414658, '010-56181910', 0, 1167324599977414658, '2022-10-28 14:59:14', 'admin', 0, '根机构');

-- ----------------------------
-- Table structure for component
-- ----------------------------
DROP TABLE IF EXISTS `component`;
CREATE TABLE `component`  (
  `componentid` decimal(20, 0) NOT NULL,
  `componentdpath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `componentcpath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `componenturl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `moduleinfoid` decimal(20, 0) NOT NULL,
  `osname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作系统名称',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of component
-- ----------------------------

-- ----------------------------
-- Table structure for customernewslog
-- ----------------------------
DROP TABLE IF EXISTS `customernewslog`;
CREATE TABLE `customernewslog`  (
  `NEWSID` bigint(20) NOT NULL COMMENT '主键id',
  `ADDRESSEE` bigint(20) NULL DEFAULT NULL COMMENT '接收用户id',
  `SENDER` bigint(20) NULL DEFAULT NULL COMMENT '发送用户id',
  `NREWSTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题内容',
  `NEWSCONTEXT` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '信息内容',
  `SENDSTATE` bigint(18) NULL DEFAULT NULL COMMENT '信息发送状态',
  `NEWSTYPE` bigint(18) NULL DEFAULT NULL COMMENT '信息类型',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `SENDTIME` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `BUSINESSTYPE` bigint(20) NULL DEFAULT NULL COMMENT '业务类型',
  `status` int(10) NULL DEFAULT NULL COMMENT '消息读取状态（0：未读；1：已读）',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `CONFIGID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息源类型',
  `ADDRESSTYPE` int(10) NULL DEFAULT NULL COMMENT '0组织，1角色，2用户',
  `ISDISPLAY` int(10) NULL DEFAULT NULL COMMENT '是否显示，0不显示，1显示（组织角色群发已发不显示）-1为假删除',
  `NAMELIST` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息接收人',
  `PARENTID` bigint(20) NULL DEFAULT NULL,
  `SEND_RANGE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送范围',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RANGE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '没用字段须删除',
  PRIMARY KEY (`NEWSID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of customernewslog
-- ----------------------------

-- ----------------------------
-- Table structure for dbfieldinfo
-- ----------------------------
DROP TABLE IF EXISTS `dbfieldinfo`;
CREATE TABLE `dbfieldinfo`  (
  `DBFIELDINFOID` decimal(20, 0) NOT NULL COMMENT '主键ID',
  `TABLENAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库表名',
  `TABLEID` decimal(20, 0) NULL DEFAULT NULL COMMENT '元数据表主键',
  `FIELDNAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段中文名',
  `ANOTHERNAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示名称',
  `FIELDTYPE` decimal(5, 0) NULL DEFAULT NULL COMMENT '字段类型',
  `DBLENGTH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据长度',
  `DEFAULTVALUE` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `ENMVALUE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '枚举值',
  `NOTBENULL` decimal(5, 0) NULL DEFAULT NULL COMMENT '是否允许空值',
  `CLASSID` decimal(20, 0) NULL DEFAULT NULL COMMENT '分类编号',
  `DBSCALE` decimal(5, 0) NULL DEFAULT NULL COMMENT '数据库字段精度',
  `VALIDATOR` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '校验器',
  `RADORCHK` decimal(5, 0) NULL DEFAULT NULL COMMENT '单选或多选项',
  `NOTEDIT` decimal(5, 0) NULL DEFAULT NULL COMMENT '是否不可编辑',
  `HIDDENFIELD` decimal(5, 0) NULL DEFAULT NULL COMMENT '是否隐藏',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` decimal(20, 0) NULL DEFAULT NULL COMMENT '创建人ID',
  `DBTYPE` decimal(5, 0) NULL DEFAULT NULL COMMENT '数据库类型',
  `FIELDORDER` decimal(20, 0) NULL DEFAULT NULL COMMENT '字段顺序',
  `GROUPID` decimal(20, 0) NULL DEFAULT NULL COMMENT '分组id',
  `NOTE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注释',
  `CLASSTYPE` int(10) NULL DEFAULT NULL,
  `generatetime` datetime NULL DEFAULT NULL COMMENT '生成时间',
  `generatestate` decimal(5, 0) NULL DEFAULT 0 COMMENT '0未生成 1生成',
  `CLASSPARENTID` decimal(20, 0) NULL DEFAULT NULL COMMENT '父分类编号',
  `SHOW_LIST` int(1) NOT NULL DEFAULT 0 COMMENT '列表展示{0:不展示,1:展示}',
  `SHOW_SEARCH` int(1) NOT NULL DEFAULT 0 COMMENT '搜索展示{0:不展示,1:展示}',
  `SHOW_DETAIL` int(1) NOT NULL DEFAULT 1 COMMENT '详情展示{0:不展示,1:展示}',
  `WIDTH` int(5) NULL DEFAULT NULL COMMENT '宽度',
  `ISSORT` int(2) NULL DEFAULT NULL COMMENT '是否是排序字段',
  `MATCH_TYPE` int(2) NULL DEFAULT NULL COMMENT '匹配方式',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  `JOINTABLE` bigint(20) NULL DEFAULT NULL COMMENT '关联表id',
  `JOINFIELD` bigint(20) NULL DEFAULT NULL COMMENT '关联字段id',
  `VERIFY_TYPE` int(2) NULL DEFAULT NULL COMMENT '校验类型',
  `REGULAR_STR` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '正则',
  `VERIFY_LENGTH_MIN` int(10) NULL DEFAULT NULL COMMENT '校验长度_下限',
  `VERIFY_LENGTH_MAX` int(10) NULL DEFAULT NULL COMMENT '校验类型_上限',
  `ISTITLE` int(2) NULL DEFAULT NULL COMMENT '是否是标题',
  `VERIFYINFO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '校验失败信息',
  `NOTNULL` decimal(5, 0) NULL DEFAULT NULL COMMENT '是否允许空值',
  PRIMARY KEY (`DBFIELDINFOID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '元数据字端信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dbfieldinfo
-- ----------------------------
INSERT INTO `dbfieldinfo` VALUES (1601108323026464770, 'JMETA_YLTABLE', 1601107907144445953, 'NAME', '产品名称', 1, '255', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2022-12-09 14:55:46', 1, 2, 1601108323026464770, 1601108201261625346, NULL, 0, NULL, 0, NULL, 1, 1, 1, NULL, 0, 8, 'admin', 1, '2022-12-09 14:55:46', NULL, NULL, NULL, '', NULL, NULL, 0, '', NULL);
INSERT INTO `dbfieldinfo` VALUES (1601108573304778753, 'JMETA_YLTABLE', 1601107907144445953, 'COLOR', '产品颜色', 6, '5', '', '黑,白', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2022-12-09 14:56:46', 1, 2, 1601108573304778753, 1601108201261625346, NULL, 0, NULL, 0, NULL, 0, 0, 1, NULL, 0, 1, 'admin', 1, '2022-12-09 14:56:46', NULL, NULL, NULL, '', NULL, NULL, 0, '', NULL);
INSERT INTO `dbfieldinfo` VALUES (1601109373628313602, 'JMETA_YLTABLE', 1601107907144445953, 'SIZE', '产品大小', 13, '200', '', '', NULL, 1601109046279663617, NULL, NULL, NULL, NULL, NULL, 'admin', '2022-12-09 14:59:57', 1, 2, 1601109373628313602, 1601108201261625346, NULL, 0, NULL, 0, 1601108948321693697, 0, 0, 1, NULL, 0, 1, 'admin', 1, '2022-12-09 14:59:57', NULL, NULL, NULL, '', NULL, NULL, 0, '', NULL);

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `no` int(11) NULL DEFAULT NULL COMMENT '编号',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 305 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dict
-- ----------------------------
INSERT INTO `dict` VALUES (1, 'field_type', 1, '2', '普通文本', 1, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2022-10-20 06:19:09');
INSERT INTO `dict` VALUES (2, 'field_type', 2, '2', '密码文本', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (3, 'field_type', 3, '5', '多行文本', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-06-29 10:29:52');
INSERT INTO `dict` VALUES (4, 'field_type', 4, '4', '日期', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (5, 'field_type', 5, NULL, '自定义', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (6, 'field_type', 6, '2', '单选框', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (7, 'field_type', 7, '2', '多选框', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (8, 'field_type', 8, '2', '下拉选择框', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (12, 'field_type', 13, '2', '分类法', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (18, 'field_type', 14, '2', '视频', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (19, 'field_type', 15, '2', '图片', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (20, 'field_type', 16, '2', '音频', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (21, 'field_type', 17, '2', '文件', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (22, 'field_type', 18, '5', '大文本编辑器', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (23, 'field_type', 19, '1', '访问量', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2020-04-21 11:56:09');
INSERT INTO `dict` VALUES (24, 'field_type111', 30, '-1', '关联单条', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2022-10-20 06:20:20');
INSERT INTO `dict` VALUES (25, 'field_type111', 31, '-1', '关联多条', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2022-10-20 06:20:21');
INSERT INTO `dict` VALUES (100, 'db_type_mysql', 1, 'BIGINT', '整数', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2021-01-14 10:58:59');
INSERT INTO `dict` VALUES (101, 'db_type_mysql', 2, 'VARCHAR', '字符串', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2021-01-14 10:59:00');
INSERT INTO `dict` VALUES (102, 'db_type_mysql', 3, 'DOUBLE', '小数', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2021-01-14 10:59:01');
INSERT INTO `dict` VALUES (103, 'db_type_mysql', 4, 'DATETIME', '时间', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2021-01-14 10:59:01');
INSERT INTO `dict` VALUES (104, 'db_type_mysql', 5, 'LONGTEXT', '大文本', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2021-01-14 10:59:02');
INSERT INTO `dict` VALUES (200, 'db_type_oracle', 1, 'NUMBER', '整数', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2021-01-14 10:59:59');
INSERT INTO `dict` VALUES (201, 'db_type_oracle', 2, 'VARCHAR', '字符串', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-01-14 10:59:13');
INSERT INTO `dict` VALUES (202, 'db_type_oracle', 3, 'NUMBER', '小数', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-01-14 10:59:15');
INSERT INTO `dict` VALUES (203, 'db_type_oracle', 4, 'DATE', '时间', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-01-14 10:59:18');
INSERT INTO `dict` VALUES (204, 'db_type_oracle', 5, 'NCLOB', '大文本', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-01-14 10:59:20');
INSERT INTO `dict` VALUES (300, 'db_type_dm', 1, 'NUMBER', '整数', NULL, 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, NULL, 1167324599977414658, '2021-01-14 10:59:59');
INSERT INTO `dict` VALUES (301, 'db_type_dm', 2, 'VARCHAR', '字符串', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-01-14 10:59:13');
INSERT INTO `dict` VALUES (302, 'db_type_dm', 3, 'NUMBER', '小数', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-01-14 10:59:15');
INSERT INTO `dict` VALUES (303, 'db_type_dm', 4, 'DATE', '时间', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-01-14 10:59:18');
INSERT INTO `dict` VALUES (304, 'db_type_dm', 5, 'NCLOB', '大文本', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-01-14 10:59:20');

-- ----------------------------
-- Table structure for documentinfo
-- ----------------------------
DROP TABLE IF EXISTS `documentinfo`;
CREATE TABLE `documentinfo`  (
  `DOCID` decimal(20, 0) NOT NULL COMMENT '主键ID',
  `DOCCHANNELID` decimal(20, 0) NOT NULL COMMENT '栏目ID',
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `DOCSOURCE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文档来源表ID',
  `DOCSTATUS` decimal(2, 0) NULL DEFAULT NULL COMMENT '有默认值 新稿，草稿 对应文档状态表',
  `DOCCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '编辑器的纯文本内容',
  `DOCHTMLCON` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '编辑器的带html标签内容',
  `DOCABSTRACT` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容摘要',
  `DOCKEYWORDS` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '关键词要',
  `SUBDOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `DOCPEOPLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '首页标题',
  `ATTACHPIC` decimal(2, 0) NULL DEFAULT NULL COMMENT '是否附图',
  `DOCAUTHOR` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者',
  `DOCEDITOR` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编辑',
  `DOCVALID` datetime NULL DEFAULT NULL COMMENT '有效时间',
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布后的链接地址',
  `DOCPUBTIME` datetime NULL DEFAULT NULL COMMENT '仅发布时添加',
  `DOCRELTIME` datetime NULL DEFAULT NULL COMMENT '撰写时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仅新建时添加',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '仅新建时添加',
  `CRNUMBER` decimal(20, 0) NULL DEFAULT NULL COMMENT '仅新建时添加',
  `DOCVERSION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文档版本',
  `DOCFROMVERSION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '从哪个版本恢复',
  `OPERTIME` datetime NULL DEFAULT NULL COMMENT '新建和修改都更改',
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '新建和修改都更改',
  `HITSCOUNT` decimal(20, 0) NULL DEFAULT NULL COMMENT '点击数',
  `DOCWORDSCOUNT` decimal(20, 0) NULL DEFAULT NULL COMMENT '纯文本内容单词数',
  `CLASSINFOID` decimal(2, 0) NULL DEFAULT NULL COMMENT '所属分类',
  `CAT1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'CAT1',
  `CAT2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'CAT1',
  `CAT3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'CAT1',
  `WRITETIME` datetime NULL DEFAULT NULL COMMENT 'WRITETIME(保留不用)',
  `TAG` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'tag词',
  `SMARTUPLOADERSTATUS` decimal(2, 0) NULL DEFAULT NULL COMMENT '默认0(现数据库默认值为1)',
  `ISUPLOADER` decimal(2, 0) NULL DEFAULT NULL COMMENT '默认0  未上传0 已上传1 软删除2\r\n（现数据库默认值为0）',
  `SINGLETEMPKATE` decimal(20, 0) NULL DEFAULT NULL COMMENT '模板表主键',
  `SITEID` decimal(20, 0) NOT NULL COMMENT '站点信息表主键,为了站点下显示文档方便？',
  `LINKURL` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文档链接地址',
  `doctype` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `searchUrl` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `typeIndex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bannerLink` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `flow` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `strong` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qwe` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`DOCID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文档信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of documentinfo
-- ----------------------------

-- ----------------------------
-- Table structure for extfield
-- ----------------------------
DROP TABLE IF EXISTS `extfield`;
CREATE TABLE `extfield`  (
  `EXTFIELDID` decimal(20, 0) NOT NULL COMMENT '主键pk',
  `TABLENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '默认扩展字段只作用于DOCUMENTINFO表 ',
  `FIELDNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段中文名称',
  `FIELDTYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库里数据类型',
  `FIELDDEFAULT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` decimal(20, 0) NULL DEFAULT NULL COMMENT '创建人ID',
  `modify_by` decimal(20, 0) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` date NULL DEFAULT NULL COMMENT '最后修改时间',
  `FIELDMAXLEN` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最大长度',
  `FIELDNULLABLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否允许空值',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`EXTFIELDID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of extfield
-- ----------------------------
INSERT INTO `extfield` VALUES (15138526435951686, 'DOCUMENTINFO', 'doctype', '2', '', 'admin', '2017-12-21 18:34:34', 201, 1167324599977414658, '2020-04-21', '100', '1', 'admin');
INSERT INTO `extfield` VALUES (15144216729031757, 'DOCUMENTINFO', 'searchUrl', '2', '', 'admin', '2017-12-28 08:38:23', 201, 1167324599977414658, '2020-04-21', '500', '1', 'admin');
INSERT INTO `extfield` VALUES (15144217318071759, 'DOCUMENTINFO', 'typeIndex', '2', '', 'admin', '2017-12-28 08:39:24', 201, 1167324599977414658, '2020-04-21', '10', '1', 'admin');
INSERT INTO `extfield` VALUES (15144516323531911, 'DOCUMENTINFO', 'bannerLink', '2', '', 'admin', '2017-12-28 16:57:45', 201, 1167324599977414658, '2020-04-21', '100', '0', 'admin');
INSERT INTO `extfield` VALUES (15145289228183172, 'DOCUMENTINFO', 'flow', '2', '', 'admin', '2017-12-29 14:25:56', 201, 1167324599977414658, '2020-04-21', '100', '0', 'admin');
INSERT INTO `extfield` VALUES (15145289485543174, 'DOCUMENTINFO', 'strong', '2', '', 'admin', '2017-12-29 14:26:22', 201, 1167324599977414658, '2020-04-21', '100', '0', 'admin');
INSERT INTO `extfield` VALUES (15145289772763176, 'DOCUMENTINFO', 'offline', '2', '', 'admin', '2017-12-29 14:26:51', 201, 1167324599977414658, '2020-04-21', '100', '0', 'admin');
INSERT INTO `extfield` VALUES (15634369220040440, 'DOCUMENTINFO', 'qwe', '2', '', 'admin', '2019-07-18 16:01:12', 201, 1167324599977414658, '2020-04-21', '10', '0', 'admin');
INSERT INTO `extfield` VALUES (1564407852234321921, 'string', 'string', 'string', 'string', 'string', '2022-08-30 08:21:12', 1, NULL, NULL, 'string', 'string', 'admin');

-- ----------------------------
-- Table structure for flow
-- ----------------------------
DROP TABLE IF EXISTS `flow`;
CREATE TABLE `flow`  (
  `id` bigint(20) NOT NULL,
  `definition_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` bigint(20) NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `fdesc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of flow
-- ----------------------------
INSERT INTO `flow` VALUES (1, 'CheckWaterMeterPlan:1:4', 1167269109998219266, 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', '123123123', 'admin');
INSERT INTO `flow` VALUES (2, 'CheckWaterMeterPlan:2:2504', 1167262122606522370, 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', '123123123123', 'admin');
INSERT INTO `flow` VALUES (3, 'test1:1:1e6e8122-cb19-11e9-bce4-0242c0a8000d', 1167262122606522370, 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', '123', 'admin');
INSERT INTO `flow` VALUES (4, 'a2:1:1e6e5a11-cb19-11e9-bce4-0242c0a8000d', 1167269018746941441, 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', '123123', 'admin');
INSERT INTO `flow` VALUES (1167653225680146434, 'new:1:75b90540-cba6-11e9-bce4-0242c0a8000d', 1167269018746941441, 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', '', 'admin');

-- ----------------------------
-- Table structure for flow_archived
-- ----------------------------
DROP TABLE IF EXISTS `flow_archived`;
CREATE TABLE `flow_archived`  (
  `id` bigint(32) NOT NULL,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程名称',
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程版本号',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程类别',
  `proc_def_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程定义id',
  `proc_inst_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
  `start_time` datetime NULL DEFAULT NULL COMMENT '流程开始时间',
  `start_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程发起人',
  `start_act_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始节点id',
  `start_for` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程发起原因',
  `end_time` datetime NULL DEFAULT NULL COMMENT '流程结束时间',
  `end_act_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束节点id',
  `end_result` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程处理结果',
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(18) NULL DEFAULT NULL COMMENT '创建人id',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流程归档表（存储已归档的流程）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of flow_archived
-- ----------------------------
INSERT INTO `flow_archived` VALUES (1255666836706631682, NULL, NULL, NULL, NULL, '00abbd0d-79c3-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 20:14:38', 1167324599977414658, 1167324599977414658, '2020-04-29 20:14:38', 'admin');
INSERT INTO `flow_archived` VALUES (1255671025407029249, NULL, NULL, NULL, NULL, '065f84b1-79c6-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 20:31:16', 1167324599977414658, 1167324599977414658, '2020-04-29 20:31:16', 'admin');
INSERT INTO `flow_archived` VALUES (1255671046319828993, NULL, NULL, NULL, NULL, '13a09d00-72ed-11ea-9ec7-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 20:31:21', 1167324599977414658, 1167324599977414658, '2020-04-29 20:31:21', 'admin');
INSERT INTO `flow_archived` VALUES (1255677758510628866, NULL, NULL, NULL, NULL, '05e497c3-7489-11ea-9ec7-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 20:58:02', 1167324599977414658, 1167324599977414658, '2020-04-29 20:58:02', 'admin');
INSERT INTO `flow_archived` VALUES (1255677766706298882, NULL, NULL, NULL, NULL, '1fc97344-79c7-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 20:58:04', 1167324599977414658, 1167324599977414658, '2020-04-29 20:58:04', 'admin');
INSERT INTO `flow_archived` VALUES (1255677772368609282, NULL, NULL, NULL, NULL, '2b87c41f-79c4-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 20:58:05', 1167324599977414658, 1167324599977414658, '2020-04-29 20:58:05', 'admin');
INSERT INTO `flow_archived` VALUES (1255677788940304385, NULL, NULL, NULL, NULL, '2f79bed9-79c4-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 20:58:09', 1167324599977414658, 1167324599977414658, '2020-04-29 20:58:09', 'admin');
INSERT INTO `flow_archived` VALUES (1255677852395929602, NULL, NULL, NULL, NULL, '4a89f3e2-79ce-11ea-820b-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 20:58:24', 1167324599977414658, 1167324599977414658, '2020-04-29 20:58:24', 'admin');
INSERT INTO `flow_archived` VALUES (1255677867528978433, NULL, NULL, NULL, NULL, '5250bddb-79c6-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 20:58:28', 1167324599977414658, 1167324599977414658, '2020-04-29 20:58:28', 'admin');
INSERT INTO `flow_archived` VALUES (1255678669983219714, NULL, NULL, NULL, NULL, '41391c49-cb73-11e9-a4e2-0242c0a8000d', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 21:01:39', 1167324599977414658, 1167324599977414658, '2020-04-29 21:01:39', 'admin');
INSERT INTO `flow_archived` VALUES (1255678790745620482, NULL, NULL, NULL, NULL, '7a8ccc33-79c4-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 21:02:08', 1167324599977414658, 1167324599977414658, '2020-04-29 21:02:08', 'admin');
INSERT INTO `flow_archived` VALUES (1255678796856721410, NULL, NULL, NULL, NULL, '7fa51e7d-79c4-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 21:02:09', 1167324599977414658, 1167324599977414658, '2020-04-29 21:02:09', 'admin');
INSERT INTO `flow_archived` VALUES (1255678915844931585, NULL, NULL, NULL, NULL, '91e92ce9-79c4-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 21:02:38', 1167324599977414658, 1167324599977414658, '2020-04-29 21:02:38', 'admin');
INSERT INTO `flow_archived` VALUES (1255678920345419778, NULL, NULL, NULL, NULL, '93198c61-79c3-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 21:02:39', 1167324599977414658, 1167324599977414658, '2020-04-29 21:02:39', 'admin');
INSERT INTO `flow_archived` VALUES (1255678969938870274, NULL, NULL, NULL, NULL, 'b420ded4-650b-11ea-82f0-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 21:02:50', 1167324599977414658, 1167324599977414658, '2020-04-29 21:02:50', 'admin');
INSERT INTO `flow_archived` VALUES (1255678973856350209, NULL, NULL, NULL, NULL, 'ad0d7fdb-650b-11ea-82f0-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 21:02:51', 1167324599977414658, 1167324599977414658, '2020-04-29 21:02:51', 'admin');
INSERT INTO `flow_archived` VALUES (1255678981208965121, NULL, NULL, NULL, NULL, 'a1321979-79c3-11ea-817f-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 21:02:53', 1167324599977414658, 1167324599977414658, '2020-04-29 21:02:53', 'admin');
INSERT INTO `flow_archived` VALUES (1255678984929312770, NULL, NULL, NULL, NULL, 'a58e5c17-72ec-11ea-9ec7-0242c0a8000a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-04-29 21:02:54', 1167324599977414658, 1167324599977414658, '2020-04-29 21:02:54', 'admin');
INSERT INTO `flow_archived` VALUES (1298517513700696065, NULL, NULL, NULL, NULL, '0e9a7d4f-ac53-11ea-94f4-0242c0a80005', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2020-08-26 15:07:55', 1167324599977414658, 1167324599977414658, '2020-08-26 15:07:55', 'admin');

-- ----------------------------
-- Table structure for flow_focus
-- ----------------------------
DROP TABLE IF EXISTS `flow_focus`;
CREATE TABLE `flow_focus`  (
  `ID` bigint(20) NOT NULL COMMENT '主键id',
  `FLOWID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作流id',
  `USERID` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `MODIFYTIME` datetime NULL DEFAULT NULL,
  `MODIFYUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of flow_focus
-- ----------------------------
INSERT INTO `flow_focus` VALUES (1255072500264644610, '8a67fbc2-7bbc-11ea-820b-0242c0a8000a', 1167324599977414658, '2020-04-28 04:52:57', 'admin', NULL, NULL, 'admin', 1167324599977414658, 1167324599977414658, '2020-04-28 04:52:57');
INSERT INTO `flow_focus` VALUES (1255678807879352322, '6c7d2492-7bbc-11ea-820b-0242c0a8000a', 1167324599977414658, '2020-04-29 21:02:12', 'admin', NULL, NULL, 'admin', 1167324599977414658, 1167324599977414658, '2020-04-29 21:02:12');
INSERT INTO `flow_focus` VALUES (1255696254913130498, '1ddac1fe-c9ff-11e9-a4e2-0242c0a8000d', 1167324599977414658, '2020-04-29 22:11:32', 'admin', NULL, NULL, 'admin', 1167324599977414658, 1167324599977414658, '2020-04-29 22:11:32');
INSERT INTO `flow_focus` VALUES (1255698285033811970, '2385ed93-cf00-11e9-bce4-0242c0a8000d', 1167324599977414658, '2020-04-29 22:19:36', 'admin', NULL, NULL, 'admin', 1167324599977414658, 1167324599977414658, '2020-04-29 22:19:36');
INSERT INTO `flow_focus` VALUES (1255698303241285633, '693ed6ae-cae8-11e9-a4e2-0242c0a8000d', 1167324599977414658, '2020-04-29 22:19:40', 'admin', NULL, NULL, 'admin', 1167324599977414658, 1167324599977414658, '2020-04-29 22:19:40');
INSERT INTO `flow_focus` VALUES (1290802441478053890, '065f84b1-79c6-11ea-817f-0242c0a8000a', 1167324599977414658, '2020-08-05 08:10:59', 'admin', NULL, NULL, 'admin', 1167324599977414658, 1167324599977414658, '2020-08-05 08:10:59');
INSERT INTO `flow_focus` VALUES (1290933925488730114, '00abbd0d-79c3-11ea-817f-0242c0a8000a', 1167324599977414658, '2020-08-05 16:53:27', 'admin', NULL, NULL, 'admin', 1167324599977414658, 1167324599977414658, '2020-08-05 16:53:27');

-- ----------------------------
-- Table structure for flow_metadata
-- ----------------------------
DROP TABLE IF EXISTS `flow_metadata`;
CREATE TABLE `flow_metadata`  (
  `proc_inst_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '流程实例id',
  `metadata_table` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据表名',
  `metadata_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据id',
  `metadata_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据标题',
  `metadata_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据url',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建人时间',
  `CRNUMBER` bigint(18) NULL DEFAULT NULL COMMENT '创建人id',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `task_handler` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程当前处理人（临时数据）',
  PRIMARY KEY (`proc_inst_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流程实例关联的元数据信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of flow_metadata
-- ----------------------------
INSERT INTO `flow_metadata` VALUES ('02a690e7-11fa-11ed-afcd-f875a4405b4a', 'JMETA_COMPUTER', '50', NULL, NULL, 'admin', '2022-08-02 08:28:21', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('0398a7c1-0262-11ed-b7da-0242aca50202', 'JMETA_COMPUTER', '17', NULL, NULL, 'admin', '2022-07-13 12:12:31', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('054ca394-0bdd-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '33', NULL, NULL, 'admin', '2022-07-25 13:45:42', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('082b309c-03dc-11ed-a52f-0242aca50202', 'JMETA_COMPUTER', '25', NULL, NULL, 'admin', '2022-07-15 09:18:28', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('17d6736f-0bcb-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '31', NULL, NULL, 'admin', '2022-07-25 11:37:22', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('24fb84fa-1181-11ed-9dbf-f875a4405b4a', 'JMETA_COMPUTER', '49', NULL, NULL, 'admin', '2022-08-01 18:03:09', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('2b4d95a9-0a66-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '29', NULL, NULL, 'admin', '2022-07-23 17:02:25', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('30e521d0-0a69-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '30', NULL, NULL, 'admin', '2022-07-23 17:24:03', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('36e0c3cb-1240-11ed-871e-f875a4405b4a', 'JMETA_COMPUTER', '55', NULL, NULL, 'admin', '2022-08-02 16:50:53', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('3ee9e06c-0e3a-11ed-8f70-f875a4405b4a', 'JMETA_COMPUTER', '43', NULL, NULL, 'admin', '2022-07-28 13:58:05', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('45814d78-0e39-11ed-9956-f875a4405b4a', 'JMETA_COMPUTER', '42', NULL, NULL, 'admin', '2022-07-28 13:51:06', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('480f3b38-0bbf-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '32', NULL, NULL, 'admin', '2022-07-25 10:12:49', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('4b1535a8-6a37-11ed-9e97-0242aca50202', 'JMETA_COMPUTER', '27', NULL, NULL, 'admin', '2022-11-22 15:28:44', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('4e64425e-0e27-11ed-9956-f875a4405b4a', 'JMETA_COMPUTER', '41', NULL, NULL, 'admin', '2022-07-28 11:42:30', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('4ece6aec-0bb5-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '32', NULL, NULL, 'admin', '2022-07-25 09:01:26', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('5043fc14-0e18-11ed-8474-0242aca50202', 'JMETA_COMPUTER', '39', NULL, NULL, 'admin', '2022-07-28 09:55:11', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('6183d68c-0345-11ed-9e9b-0242aca50202', 'JMETA_COMPUTER', '24', NULL, NULL, 'admin', '2022-07-14 15:20:04', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('6e22c2cb-0e14-11ed-8474-0242aca50202', 'JMETA_COMPUTER', '37', NULL, NULL, 'admin', '2022-07-28 09:27:23', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('76f71d30-0340-11ed-ad4e-141333a7cc07', 'JMETA_COMPUTER', '23', NULL, NULL, 'admin', '2022-07-14 14:44:53', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('790e319f-115c-11ed-9729-f875a4405b4a', 'JMETA_COMPUTER', '45', NULL, NULL, 'admin', '2022-08-01 13:40:39', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('8e46f0e2-0c89-11ed-bb71-0242aca50202', 'JMETA_COMPUTER', '35', NULL, NULL, 'admin', '2022-07-26 10:20:46', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('9bad8fc1-1206-11ed-afcd-f875a4405b4a', 'JMETA_COMPUTER', '51', NULL, NULL, 'admin', '2022-08-02 09:58:31', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('a596488b-1226-11ed-afcd-f875a4405b4a', 'JMETA_COMPUTER', '54', NULL, NULL, 'admin', '2022-08-02 13:47:52', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('a5fe7271-0bb7-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '30', NULL, NULL, 'admin', '2022-07-25 09:18:11', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('a80e7145-115e-11ed-9729-f875a4405b4a', 'JMETA_COMPUTER', '46', NULL, NULL, 'admin', '2022-08-01 13:56:17', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('b2bd2db6-0bb2-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '30', NULL, NULL, 'admin', '2022-07-25 08:42:45', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('b33b53b3-0bb1-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '30', NULL, NULL, 'admin', '2022-07-25 08:35:36', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('b8e0605c-0bb2-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '31', NULL, NULL, 'admin', '2022-07-25 08:42:55', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('c45644b1-0e25-11ed-a264-f875a4405b4a', 'JMETA_COMPUTER', '40', NULL, NULL, 'admin', '2022-07-28 11:31:29', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('c6e415ce-04ed-11ed-8c72-0242aca50202', 'JMETA_COMPUTER', '27', NULL, NULL, 'admin', '2022-07-16 17:58:01', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('cca4dfe1-0e16-11ed-8474-0242aca50202', 'JMETA_COMPUTER', '38', NULL, NULL, 'admin', '2022-07-28 09:44:20', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('d76c3be6-0343-11ed-9e9b-0242aca50202', 'JMETA_COMPUTER', '20', NULL, NULL, 'admin', '2022-07-14 15:09:03', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('d839fef9-1162-11ed-9dbf-f875a4405b4a', 'JMETA_COMPUTER', '48', NULL, NULL, 'admin', '2022-08-01 14:26:16', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('d9db7d28-0d92-11ed-8474-0242aca50202', 'JMETA_COMPUTER', '36', NULL, NULL, 'admin', '2022-07-27 17:59:49', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('de015648-0be1-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '35', NULL, NULL, 'admin', '2022-07-25 14:20:24', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('edc93db5-0bdd-11ed-9479-0242aca50202', 'JMETA_COMPUTER', '34', NULL, NULL, 'admin', '2022-07-25 13:52:12', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('f46a99af-1225-11ed-afcd-f875a4405b4a', 'JMETA_COMPUTER', '53', NULL, NULL, 'admin', '2022-08-02 13:42:55', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('f4b29546-12dd-11ed-955e-f875a4405b4a', 'JMETA_COMPUTER', '56', NULL, NULL, 'admin', '2022-08-03 11:40:03', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('f7f70603-115f-11ed-9729-f875a4405b4a', 'JMETA_COMPUTER', '47', NULL, NULL, 'admin', '2022-08-01 14:05:40', 1167324599977414658, NULL, NULL, NULL, NULL);
INSERT INTO `flow_metadata` VALUES ('f8f71b9f-01ac-11ed-a9f4-0242aca50202', 'JMETA_COMPUTER', '16', NULL, NULL, 'admin', '2022-07-12 14:36:34', 1167324599977414658, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for flow_type
-- ----------------------------
DROP TABLE IF EXISTS `flow_type`;
CREATE TABLE `flow_type`  (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of flow_type
-- ----------------------------
INSERT INTO `flow_type` VALUES (1, '默认分类', 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', 'admin');
INSERT INTO `flow_type` VALUES (1167262122606522370, '新建类', 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', 'admin');
INSERT INTO `flow_type` VALUES (1167269018746941441, '行政类', 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', 'admin');
INSERT INTO `flow_type` VALUES (1167269049495384066, '项目类', 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', 'admin');
INSERT INTO `flow_type` VALUES (1167269109998219266, '费用类', 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', 'admin');
INSERT INTO `flow_type` VALUES (1167269247676248065, '库存类', 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', 'admin');
INSERT INTO `flow_type` VALUES (1167269269293690881, '发文类', 'admin', '2020-04-21 11:41:14', 1167324599977414658, 1167324599977414658, '2020-04-21 11:56:09', 'admin');
INSERT INTO `flow_type` VALUES (1249941406319923202, '11', 'admin', '2020-04-14 01:03:49', 1167324599977414658, 1167324599977414658, '2020-04-21 11:25:47', 'admin');

-- ----------------------------
-- Table structure for gitaccount
-- ----------------------------
DROP TABLE IF EXISTS `gitaccount`;
CREATE TABLE `gitaccount`  (
  `id` decimal(20, 0) NOT NULL COMMENT '主键id',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'git账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人Id',
  `EMAIL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'email',
  `sshkey` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ssh密匙',
  `gitpath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'gitpath',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gitaccount
-- ----------------------------

-- ----------------------------
-- Table structure for groupinfo
-- ----------------------------
DROP TABLE IF EXISTS `groupinfo`;
CREATE TABLE `groupinfo`  (
  `GROUPID` bigint(20) NOT NULL COMMENT '主键id',
  `GROUPNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织名称',
  `GROUPPATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织层级结构',
  `GDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织描述',
  `PARENTID` bigint(20) NULL DEFAULT NULL COMMENT '父组织编号',
  `GROUPORDER` bigint(10) NULL DEFAULT NULL COMMENT '组织排序字段',
  `STATUS` int(5) NULL DEFAULT NULL COMMENT '组织状态',
  `ADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织地址',
  `MOBILE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `PHONE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定电话',
  `QQ` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织qq',
  `EMAIL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织email',
  `GRPCODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织编码',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `COMPANYID` bigint(20) NULL DEFAULT NULL COMMENT '所属机构id',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `ISLEADER` int(5) NULL DEFAULT NULL COMMENT '本小组是否有组长',
  PRIMARY KEY (`GROUPID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of groupinfo
-- ----------------------------
INSERT INTO `groupinfo` VALUES (1601097990211641346, '样例组织', NULL, '', NULL, NULL, NULL, '', '', '', '', '', '', 'admin', 1, '2022-12-09 14:14:42', NULL, NULL, 1167323268476895233, NULL, NULL);

-- ----------------------------
-- Table structure for grp_user
-- ----------------------------
DROP TABLE IF EXISTS `grp_user`;
CREATE TABLE `grp_user`  (
  `GRPUSERID` bigint(20) NOT NULL COMMENT '主键id',
  `GROUPID` bigint(20) NULL DEFAULT NULL COMMENT '组织编号',
  `USERID` bigint(20) NULL DEFAULT NULL COMMENT '用户编号',
  `ISLEADER` int(5) NULL DEFAULT NULL COMMENT '是否组长',
  `USERORDER` bigint(10) NULL DEFAULT NULL COMMENT '排序字段',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `JOB` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位',
  PRIMARY KEY (`GRPUSERID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织用户关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of grp_user
-- ----------------------------
INSERT INTO `grp_user` VALUES (1601099282430566402, 1601097990211641346, 1, 0, NULL, 'admin', 1, '2022-12-09 14:19:51', NULL, NULL, NULL, NULL);
INSERT INTO `grp_user` VALUES (1601099282631892994, 1601097990211641346, 10, 0, NULL, 'admin', 1, '2022-12-09 14:19:51', NULL, NULL, NULL, NULL);
INSERT INTO `grp_user` VALUES (1601099282631892995, 1601097990211641346, 1601098737791803394, 0, NULL, 'admin', 1, '2022-12-09 14:19:51', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for j_business
-- ----------------------------
DROP TABLE IF EXISTS `j_business`;
CREATE TABLE `j_business`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(18) NULL DEFAULT NULL COMMENT '创建人id',
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0 COMMENT '删除状态',
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL COMMENT '站点id',
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态',
  `COMPANYID` bigint(20) NULL DEFAULT NULL COMMENT '所属机构id',
  `WEBSITEID` bigint(20) NULL DEFAULT NULL COMMENT '站点id',
  `COLUMNID` bigint(20) NULL DEFAULT NULL COMMENT '栏目id',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `SEQENCING` int(10) NULL DEFAULT NULL COMMENT '排序',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作流id',
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作流用户',
  `NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `LEGAL_REPRESENTATIVE` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法定代表人',
  `REGISTERED_CAPITAL` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册资本',
  `DATE_OF_ESTABLISHMENT` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '成立日期',
  `TEL` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `EMAIL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `ADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `OFFICIAL_WEBSITE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '官网',
  `FORMER_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '曾用名',
  `BRAND` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `PRODUCT` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品',
  `EN_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '英文名',
  `STAFF_SIZE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人员规模',
  `GROUPID` bigint(20) NULL DEFAULT NULL COMMENT '组织id',
  `REGISTEREADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册地址',
  `TAXPAYERNO` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纳税人识别号',
  `LEGALNUMBER` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人身份证',
  `CONTACTS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `CONTACTINFORMAT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `BUSINESS_LICENSE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '营业执照',
  `USERID` bigint(20) NULL DEFAULT NULL COMMENT '主账户用户id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_business
-- ----------------------------

-- ----------------------------
-- Table structure for j_business_user
-- ----------------------------
DROP TABLE IF EXISTS `j_business_user`;
CREATE TABLE `j_business_user`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `USERID` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `GROUPID` bigint(20) NULL DEFAULT NULL COMMENT '组织id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_business_user
-- ----------------------------

-- ----------------------------
-- Table structure for j_component_button
-- ----------------------------
DROP TABLE IF EXISTS `j_component_button`;
CREATE TABLE `j_component_button`  (
  `button_id` bigint(20) NOT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `button_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `word_content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `button_icon` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position_x` int(11) NULL DEFAULT NULL,
  `position_y` int(11) NULL DEFAULT NULL,
  `size_width` int(11) NULL DEFAULT NULL,
  `size_height` int(11) NULL DEFAULT NULL,
  `picture_round` int(11) NULL DEFAULT NULL,
  `fill_color` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_line` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `button_font` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type_setup` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`button_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_button
-- ----------------------------
INSERT INTO `j_component_button` VALUES (1539538375928299522, 'string', 'string', 'string', 'string', 100, 50, 0, 0, 0, 'string', 'string', 'string', 'string');
INSERT INTO `j_component_button` VALUES (1542320837506539521, 'string', 'string', 'string', 'string', 0, 0, 0, 0, 0, 'string', 'string', 'string', 'string');
INSERT INTO `j_component_button` VALUES (1542321091584892930, 'string', 'string', 'string', 'string', 0, 0, 0, 0, 0, 'string', 'string', 'string', 'string');
INSERT INTO `j_component_button` VALUES (1542323256722366465, 'string', 'string', 'string', 'string', 0, 0, 0, 0, 0, 'string', 'string', 'string', 'string');
INSERT INTO `j_component_button` VALUES (1542323746201837570, 'string', 'string', 'string', 'string', 0, 0, 0, 0, 0, 'string', 'string', 'string', 'string');
INSERT INTO `j_component_button` VALUES (1542328595039559682, '', 'string', '按钮', 'string', 50, 50, 99, 36, 2, 'rgb(0, 150, 136)', 'rgb(0, 150, 136)', 'string', '0,0,0,2');
INSERT INTO `j_component_button` VALUES (1547490422195122178, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547490456026378241, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547490595495374849, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547490605381349378, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547490660741967874, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547490673031278594, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547490710910038018, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547490721647456258, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547490725669793793, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547490767419895810, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547740969036869634, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547741064595697666, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547741082505375745, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547741192215785474, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547741277246910465, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547778693462683650, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547779984301682690, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547779996196728833, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547825133887090690, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547825530261401601, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547826687063027714, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547826953451663362, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547826965749362689, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547826974972637186, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547827143004844033, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547827193521041410, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547827936588132354, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547828217665220610, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547829728696791041, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547834038121852929, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547834060976615426, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547835551326072833, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547839395556683778, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547840206319841281, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547840217938063361, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547840229325598721, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547840493017296898, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547840505117863938, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547840555009110017, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547840565905911809, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547840575791886338, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547841621830332417, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1547845485606400002, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1551770756952772610, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1551770770517151746, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1551770804893667329, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1551771018207580161, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1551771153893314561, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1551774926489407490, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1552137649882550274, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1552179902210527234, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1552199809593659393, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1554031091596283906, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1554366299986071554, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1554367354631229441, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1554714002821615619, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1554718333826371586, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114060406722561, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114104979591171, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114131114299393, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114160721891330, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114174940581889, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114217617625089, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114244654108673, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114288354562049, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114312568279042, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_button` VALUES (1555114352317698050, NULL, NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for j_component_label
-- ----------------------------
DROP TABLE IF EXISTS `j_component_label`;
CREATE TABLE `j_component_label`  (
  `label_id` bigint(20) NOT NULL,
  `label_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `label_font` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `font_size` int(11) NULL DEFAULT NULL,
  `line_space` int(11) NULL DEFAULT NULL,
  `label_typesetting` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_label
-- ----------------------------
INSERT INTO `j_component_label` VALUES (1541294087892791297, 'string', '宋体', 0, 0, 'string');
INSERT INTO `j_component_label` VALUES (1544584111820288002, 'string', 'string', 0, 0, 'string');
INSERT INTO `j_component_label` VALUES (1544619258284257281, 'string', 'string', 0, 0, 'string');
INSERT INTO `j_component_label` VALUES (1544861967641128961, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544862397683118082, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544936026752069634, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544939551947141122, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544939879794913281, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544939957473423361, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544940202940870658, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544940349036867585, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544940789510090753, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544940940551172098, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544941261692252162, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544941349944602626, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544941605788758018, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544942110254477313, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544942215460204546, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544942275224842242, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544942335912226817, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544942425448034305, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1544945041359941633, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545247355752853506, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545256261753417729, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545297897350934529, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545300393460936706, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545301563046801410, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545301917020893185, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545303424470528001, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545303580490248193, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545306802688798721, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545308187220160514, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545312582003503105, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545642639733469185, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545683728557375489, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1545684151351607298, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1547490456299008001, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1547490595646369794, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1547490767487004674, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1547779984872108033, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1547792652379025409, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1547792673145024514, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1547792890972008449, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1551770756885663746, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1551770770743644161, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1551770805044662273, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1551771018354380802, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1551771153973006337, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1551771416519659522, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1551774926686539778, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1552199809774014466, NULL, NULL, 0, 0, NULL);
INSERT INTO `j_component_label` VALUES (1554367355121963010, NULL, NULL, 0, 0, NULL);

-- ----------------------------
-- Table structure for j_component_login
-- ----------------------------
DROP TABLE IF EXISTS `j_component_login`;
CREATE TABLE `j_component_login`  (
  `login_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录类型，关联分类法',
  `login_third` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方登录，关联分类法',
  `verify_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证方式，关联分类法',
  `login_to` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录跳转至，1-站内链接，2-站内链接',
  `link_menu_id` bigint(20) NULL DEFAULT NULL COMMENT '（站内链接）菜单id\r\n（站内链接）菜单id\r\n（站内链接）菜单id',
  `link_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '（站外链接）链接地址',
  PRIMARY KEY (`login_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_login
-- ----------------------------
INSERT INTO `j_component_login` VALUES (6, '登陆类型', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `j_component_login` VALUES (7, '登陆类型', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for j_component_navigation
-- ----------------------------
DROP TABLE IF EXISTS `j_component_navigation`;
CREATE TABLE `j_component_navigation`  (
  `navigation_id` bigint(20) NOT NULL,
  `side_expansion` int(11) NULL DEFAULT NULL,
  `page_refresh` int(11) NULL DEFAULT NULL,
  `current_location` int(11) NULL DEFAULT NULL,
  `user_information` int(11) NULL DEFAULT NULL,
  `change_password` int(11) NULL DEFAULT NULL,
  `full_screenview` int(11) NULL DEFAULT NULL,
  `log_out` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`navigation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_navigation
-- ----------------------------
INSERT INTO `j_component_navigation` VALUES (1541299355318345729, 0, 0, 0, 1, 0, 1, 0);
INSERT INTO `j_component_navigation` VALUES (1541678861107445761, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1542037830023421954, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1542038517423710209, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1542038553415032833, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1542039698988507138, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542039727321030658, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542040342105333761, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1542040443968200705, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1542040617448808449, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542040952569503745, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542041077161304066, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542041111407796225, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542042343383932929, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542042395905007617, 0, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542042418650718209, 0, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542042741196890113, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1542042962282848258, 1, 0, 1, 0, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542043143854268418, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1542043429314404354, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542044293131313153, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542044381777928194, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542044895282372610, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542045068653928450, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542045631516942337, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542046252127133698, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542046312265064449, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542046506733969409, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542047629268131841, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542047735161724930, 1, 0, 1, 0, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542047987553968130, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542048026967842818, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542048194484150274, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542048528501743618, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542048816939835394, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542050012631048193, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542050053848473601, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542050544200359938, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542050671044501505, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542050763335966722, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542050917732491266, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542051252685414401, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542051308398354433, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542051747672006657, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542052024412184577, 0, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542054310685351937, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1542305381567602689, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1544581023243874305, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1544581413779714050, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1544581604545048577, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1544932969079287810, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1544935168354201601, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1544946378541805570, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1545299776063582209, 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `j_component_navigation` VALUES (1547490456319979522, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1547490595604426753, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1547490767482810369, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1547779984532369409, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1547792652328693762, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1547792672968863745, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1547792890837790721, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1551770756797583362, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1551770770559094785, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1551770804977553410, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1551771018266300418, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1551771153876537345, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1551771416465133569, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1551774926489407491, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1552199809790791681, 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `j_component_navigation` VALUES (1554367355155517442, 0, 0, 0, 0, 0, 0, 0);

-- ----------------------------
-- Table structure for j_component_picture
-- ----------------------------
DROP TABLE IF EXISTS `j_component_picture`;
CREATE TABLE `j_component_picture`  (
  `picture_id` bigint(20) NOT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position_x` int(11) NULL DEFAULT NULL,
  `position_y` int(11) NULL DEFAULT NULL,
  `size_width` int(11) NULL DEFAULT NULL,
  `size_height` int(11) NULL DEFAULT NULL,
  `picture_round` int(11) NULL DEFAULT NULL,
  `picture_url` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`picture_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_picture
-- ----------------------------
INSERT INTO `j_component_picture` VALUES (1537618285129408513, 'milk', 100, 50, 0, 0, 0, 'string');
INSERT INTO `j_component_picture` VALUES (1541679967065718785, 'www', 10, 10, 10, 10, 0, 'stqeqwed');
INSERT INTO `j_component_picture` VALUES (1541681364133855233, 'www', 10, 10, 10, 10, 0, 'qwqwedqwdqwdwqdsacasddasd');
INSERT INTO `j_component_picture` VALUES (1541681418106159106, 'www', 10, 10, 10, 10, 0, 'QWQWQWQWQWQWQWQWQWqwqwedqwdqwdwqdsacasddasdcsacascaxcaxcaQWQWQWWQ');
INSERT INTO `j_component_picture` VALUES (1541681444144398338, 'www', 10, 10, 10, 10, 0, 'QWQWQWQW/QWQWQWQWQWqwqwedqwdqwdwqdsacasddasdcsacascaxcaxcaQWQWQWWQ');
INSERT INTO `j_component_picture` VALUES (1541681470602067969, 'www', 10, 10, 10, 10, 0, 'QWQWQWQW/QWQWQWQWQWqwqwed/qwdqwdwqdsacasddasdcsacascaxcaxcaQWQWQWWQ');
INSERT INTO `j_component_picture` VALUES (1541681643491278849, 'www', 10, 10, 10, 10, 0, 'QWQWQWQW/QWQW的地位QWQWQWqwqwed/qwdqwdwqdsacasddasdcsacascaxcaxcaQWQWQWWQ');
INSERT INTO `j_component_picture` VALUES (1541681693214752769, 'www', 10, 10, 10, 10, 0, 'C:\\Users\\Administrator\\Desktop\\日报');
INSERT INTO `j_component_picture` VALUES (1541688865730445313, 'SSSS', 10, 10, 10, 10, 0, 'C:');
INSERT INTO `j_component_picture` VALUES (1541947881609691137, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541947930066485250, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541949295639592961, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541949991436877825, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541952300304760834, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541952523756306434, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541952587673305089, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541952732401958914, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541953235219316738, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541959244478349314, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541960090700152834, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541961576070971394, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541962485509324802, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541964257841840130, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541966300266561538, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541967188649508865, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1541967188775337985, '', 50, 50, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1542084025571778561, '', 20, 73, 82, 41, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1542084026494525442, '', 135, 228, 150, 0, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1542694109477740546, '', 345, 683, 100, 100, 0, '../upload/images/logo1.png');
INSERT INTO `j_component_picture` VALUES (1544201082102865921, 'string', 0, 0, 0, 0, 0, 'string');
INSERT INTO `j_component_picture` VALUES (1547490422409031682, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490422425808897, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490605599453186, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490605800779778, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490660586778625, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490660712607745, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490673081610241, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490673127747586, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490711102976001, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490711270748162, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490721878142977, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490722054303746, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490725833371649, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547490725975977986, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547740969049452545, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547740969057841153, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547741064759275522, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547741064952213506, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547741082408906753, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547741082429878273, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547741192228368385, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547741192350003202, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547741277423071233, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547741277595037697, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547778693445906433, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547778693462683651, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547779996385472513, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547779996469358594, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547792652307722242, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547792672872394754, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547792890749710338, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547825133991948290, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547825134038085634, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547825530500476930, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547825530701803522, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547826687180468226, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547826687285325826, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547826953929814018, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547826954361827330, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547826965988438018, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547826966143627266, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547826975144603649, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547826975383678977, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547827143214559234, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547827144208609281, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547827193718173698, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547827193986609154, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547827936831401985, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547827936856567810, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547828217665220609, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547828217761689602, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547829729841836034, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547829730324180994, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547834038272847873, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547834038369316866, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547834061274411010, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547834061584789505, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547835551485456385, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547835551619674114, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547839395774787585, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547839395871256577, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840206504390657, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840206688940034, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840218164555777, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840218353299458, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840229522731010, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840229682114561, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840493688385537, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840493822603265, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840505306607618, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840505495351298, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840555269156865, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840555336265729, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840566124015617, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840566304370689, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840576026767361, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547840576228093954, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547841621880664065, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547841621964550146, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547845486231351297, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547845486235545601, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1547847525065748482, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1551771416737763329, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1552137650155180033, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1552137650276814849, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1552179902411853825, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1552179902546071553, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1554031091537563650, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1554031092082823169, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1554366299826688002, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1554366300204175362, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1554714002985193474, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1554714003215880193, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1554718333977366529, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1554718334036086786, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114060473831427, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114060880678914, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114105180917762, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114105336107010, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114131336597505, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114131491786753, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114160889663489, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114161049047041, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114175158685698, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114175376789506, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114217844117506, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114218041249794, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114244830269441, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114244989652993, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114288560082945, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114288740438017, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114312903823362, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114313759461378, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114352519024641, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555114352720351234, NULL, 0, 0, 0, 0, 0, NULL);
INSERT INTO `j_component_picture` VALUES (1555429469618049025, NULL, 0, 0, 0, 0, 0, NULL);

-- ----------------------------
-- Table structure for j_component_rectangle
-- ----------------------------
DROP TABLE IF EXISTS `j_component_rectangle`;
CREATE TABLE `j_component_rectangle`  (
  `rectangle_id` bigint(20) NOT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position_x` int(11) NULL DEFAULT NULL,
  `position_y` int(11) NULL DEFAULT NULL,
  `size_width` int(11) NULL DEFAULT NULL,
  `size_height` int(11) NULL DEFAULT NULL,
  `picture_round` int(11) NULL DEFAULT NULL,
  `line_width` int(11) NULL DEFAULT NULL,
  `rectangle_fill` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rectangle_color` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rectangle_edge` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`rectangle_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_rectangle
-- ----------------------------
INSERT INTO `j_component_rectangle` VALUES (1541971416994811906, 'string', 0, 0, 0, 0, 0, 10, 'string', 'string', 'string');
INSERT INTO `j_component_rectangle` VALUES (1541978643759276033, 'string', 0, 0, 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_rectangle` VALUES (1542306725615542273, 'string', 0, 0, 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_rectangle` VALUES (1542307395479445506, 'string', 0, 0, 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_rectangle` VALUES (1542308751304990721, 'string', 0, 0, 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_rectangle` VALUES (1542310898146910209, 'string', 0, 0, 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_rectangle` VALUES (1542441331970940930, '', 148, 211, 700, 43, 0, 5, 'rgb(194, 56, 56)', 'rgb(0, 150, 136)', '1');
INSERT INTO `j_component_rectangle` VALUES (1542677133908910081, '', 205, 197, 700, 43, 10, 11, 'rgb(52, 167, 17)', 'rgb(13, 201, 44)', '1');
INSERT INTO `j_component_rectangle` VALUES (1542682456455520258, '', 93, 200, 700, 43, 0, 5, 'rgb(87, 205, 19)', 'rgb(231, 50, 22)', '1');
INSERT INTO `j_component_rectangle` VALUES (1542684058218274817, '', 248, 271, 700, 43, 5, 5, 'rgb(159, 201, 19)', 'rgb(0, 150, 136)', '1');
INSERT INTO `j_component_rectangle` VALUES (1542689997298212865, '', 240, 270, 700, 43, 0, 5, 'rgb(70, 29, 232)', 'rgb(0, 150, 136)', '1');
INSERT INTO `j_component_rectangle` VALUES (1542695027824795650, '', 105, 315, 700, 43, 0, 5, 'rgb(249, 249, 249)', 'rgb(0, 150, 136)', '1');
INSERT INTO `j_component_rectangle` VALUES (1542800020867260417, '', 112, 316, 700, 43, 0, 5, 'rgb(249, 249, 249)', 'rgb(0, 150, 136)', '1');
INSERT INTO `j_component_rectangle` VALUES (1543101320079220738, '', 155, 269, 700, 43, 0, 5, 'rgb(80, 228, 43)', 'rgb(0, 150, 136)', '1');
INSERT INTO `j_component_rectangle` VALUES (1543154580391600130, '', 60, 339, 700, 43, 0, 5, 'rgb(196, 20, 20)', 'rgb(0, 150, 136)', '1');
INSERT INTO `j_component_rectangle` VALUES (1544498225069862913, '', 50, 50, 700, 43, 0, 5, 'rgb(249, 249, 249)', 'rgb(0, 150, 136)', '1');
INSERT INTO `j_component_rectangle` VALUES (1544511907132780545, '', 50, 50, 700, 43, 0, 5, 'rgb(249, 249, 249)', 'rgb(0, 150, 136)', '1');
INSERT INTO `j_component_rectangle` VALUES (1547792652093812738, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1547792652106395650, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1547792652106395652, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1547792672272609281, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1547792672356495361, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1547792672536850433, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1547792890137341953, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1547792890288336897, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1547792890468691970, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1551771416309944321, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1551771416330915842, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_rectangle` VALUES (1551771416381247490, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for j_component_search
-- ----------------------------
DROP TABLE IF EXISTS `j_component_search`;
CREATE TABLE `j_component_search`  (
  `search_id` bigint(20) NOT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属菜单id',
  `db_table_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联的元数据表id',
  `search_relation` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '搜索关系，关联分类法',
  PRIMARY KEY (`search_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_search
-- ----------------------------

-- ----------------------------
-- Table structure for j_component_tab
-- ----------------------------
DROP TABLE IF EXISTS `j_component_tab`;
CREATE TABLE `j_component_tab`  (
  `tab_id` bigint(20) NOT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `module_style` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position_x` int(11) NULL DEFAULT NULL,
  `position_y` int(11) NULL DEFAULT NULL,
  `size_width` int(11) NULL DEFAULT NULL,
  `size_height` int(11) NULL DEFAULT NULL,
  `background_color` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_line` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `roll_setup` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`tab_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_tab
-- ----------------------------
INSERT INTO `j_component_tab` VALUES (1539438476775772162, 'string', 'string', 50, 0, 0, 0, 'black', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1540241993174142978, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544212219380883457, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544219831761547265, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544491812151013378, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544491851606831105, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544491874415456258, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544492441091092482, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544492997088030722, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544493011076034562, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544493432066715650, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544493678142337025, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544493690876243970, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544493738171215873, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544493902093004801, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544494096595464193, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544494285754380290, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544494389945085953, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544494473327849473, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544494552579223554, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544494623731396610, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544494641494274049, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544495296002830338, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544495639000428545, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544495986079084545, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544497126162214913, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544497141827940354, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544497449358503937, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544497659421831170, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544497838262759425, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544503699135705090, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544503712993685506, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544503864210927618, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544504383562231810, '', NULL, 220, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544504541188370433, '', NULL, 199, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544504561467830274, '', NULL, 199, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544505608705847297, '', NULL, 90, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544506200731856898, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544506349508014082, '', NULL, 183, 48, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544506738974306305, '', NULL, 141, 41, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544506896671748097, '', NULL, 110, 35, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544506905991491586, '', NULL, 110, 35, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544507076938739713, '', NULL, 110, 35, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544507310314008578, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544507386851667970, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544507422541000705, '', NULL, 232, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544507546835005441, '', NULL, 124, 1, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544507610907193346, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544507719015378945, '', NULL, 69, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544507951455318017, '', NULL, 68, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544507973043400705, '', NULL, 68, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544508311892832258, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544508334776954881, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544508631981142017, '', NULL, 188, 32, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544508755595669506, '', NULL, 89, 45, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544508860948197377, '', NULL, 215, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544509278298222594, '', NULL, 204, 9, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544509353934106626, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544509942336237570, '', NULL, 180, 32, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544510244506480642, '', NULL, 211, 4, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544511045433995265, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544512462014033921, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544512548089540609, '', NULL, 500, 50, 500, 500, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544514036073410562, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544514357545840641, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544514398943621121, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544515499298304001, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544517127195766786, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544518309725253634, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544519275094650881, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544520800667873282, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544522032312659970, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544582925889220610, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544583136317452290, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544583728909692929, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585196660240386, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585238552948737, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585238662000642, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585264662491137, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585264712822785, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585287861186561, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585287911518209, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585320115384322, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585320178298882, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544585343507017730, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544612182761844738, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544612197072809986, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544616798782140418, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544616932173590529, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544617317709819905, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544617528112885762, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544617548199407617, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544617585209946113, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544919298424877058, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544919535277223938, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544919706702622722, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544922792963780609, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544923652997427202, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544923994191474690, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544929697689939970, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544929740387954690, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544931271875145729, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544931389101748225, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544931587299389442, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544933027136843777, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544933495275696129, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544933919084949505, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544934327643713538, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544934416462295041, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1544935143649751042, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544939096240205825, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1544939210534989826, 'string', 'string', 0, 0, 0, 0, 'string', 'string', 'string');
INSERT INTO `j_component_tab` VALUES (1545684151582294017, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490421985406978, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490455808274434, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490595377934337, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490605263908866, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490660540641282, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490673031278595, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490710763237377, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490721550987265, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490725573324801, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547490767356981249, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547740968994926593, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547741064486645761, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547741082320826370, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547741192215785473, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547741277158830081, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547778693320077314, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547779984305876993, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547779996117037058, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547825133757067265, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547825530114600962, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547826686865895425, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547826953271308289, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547826965615144962, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547826974867779585, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547827142765768706, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547827193353269249, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547827936483274753, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547828215891030018, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547829728608710657, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547834038092492801, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547834060846592001, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547835551154106369, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547839395418271745, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547840206063988737, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547840217833205762, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547840229220741121, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547840492878884866, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547840504962674689, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547840554803589122, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547840565746528257, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547840575670251521, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547841621662560258, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1547845485463793665, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1551770756965355522, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1551770770437459970, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1551770804692340738, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1551771017670709249, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1551771153729736705, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1551774926682345474, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1552137649635086338, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1552179902038560769, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1552199809379749890, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1554031091621449729, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1554366300032208897, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1554367354572509186, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1554714002452516865, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1554718333734096898, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114060473831426, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114104836984834, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114131001053186, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114160596062210, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114174831529985, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114217470824449, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114244545056769, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114288258093057, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114312455032834, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `j_component_tab` VALUES (1555114352204451841, NULL, NULL, 0, 0, 0, 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for j_component_table
-- ----------------------------
DROP TABLE IF EXISTS `j_component_table`;
CREATE TABLE `j_component_table`  (
  `table_id` bigint(20) NOT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属菜单id',
  `table_style` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表格样式，关联分类法',
  `position_x` int(11) NULL DEFAULT NULL COMMENT '位置X',
  `position_y` int(11) NULL DEFAULT NULL COMMENT '位置Y',
  `size_width` int(11) NULL DEFAULT NULL COMMENT '高度',
  `size_height` int(11) NULL DEFAULT NULL COMMENT '宽度',
  `table_font` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字体，关联分类法',
  `margin_left` int(11) NULL DEFAULT NULL COMMENT '边距左',
  `margin_top` int(11) NULL DEFAULT NULL COMMENT '边距上',
  `margin_right` int(11) NULL DEFAULT NULL COMMENT '边距右',
  `margin_bottom` int(11) NULL DEFAULT NULL COMMENT '边距下',
  `type_setup` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排版，1-左对齐，2-居中，3-右对齐',
  `select_setup` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选择设置，关联分类法',
  `page_setup` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分页设置，关联分类法',
  `page_size` int(11) NULL DEFAULT NULL COMMENT '每页显示数据条数',
  `db_table_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联的元数据表id',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_table
-- ----------------------------

-- ----------------------------
-- Table structure for j_component_text
-- ----------------------------
DROP TABLE IF EXISTS `j_component_text`;
CREATE TABLE `j_component_text`  (
  `text_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `text_content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position_x` int(11) NULL DEFAULT NULL,
  `position_y` int(11) NULL DEFAULT NULL,
  `size_width` int(11) NULL DEFAULT NULL,
  `size_height` int(11) NULL DEFAULT NULL,
  `text_font` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `font_size` int(11) NULL DEFAULT NULL,
  `line_space` int(11) NULL DEFAULT NULL,
  `type_setup` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`text_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1555451988504875010 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_text
-- ----------------------------
INSERT INTO `j_component_text` VALUES (1537365389179711489, '2', '姓名=张三', 2, 2, 2, 2, '黑体', 1, 2, 'string');
INSERT INTO `j_component_text` VALUES (1537365595363307522, '2', 'string', 2, 2, 2, 2, '宋体', 7, 2, 'string');
INSERT INTO `j_component_text` VALUES (1537375439289868290, '2', '姓名=李四', 2, 2, 2, 2, '楷体', 10, 2, 'string');
INSERT INTO `j_component_text` VALUES (1540245273086345217, 'string', 'string', 0, 0, 0, 0, 'string', 0, 0, 'string');
INSERT INTO `j_component_text` VALUES (1540247361803227137, 'string', 'string', 0, 0, 0, 0, 'string', 0, 0, 'string');
INSERT INTO `j_component_text` VALUES (1540247702993080321, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248023467266049, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248111946108930, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248141083938817, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248284940177410, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248391781683201, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248432520957953, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248461159665666, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248498379919362, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248682648276994, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248891621085185, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540248937498382338, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540254972619493378, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540255310411960322, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540255643158679553, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540255687232425986, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540256263668207618, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540256388952068097, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540256592874934274, '', NULL, 50, 50, 100, 100, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540257114684100609, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540257196678549505, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540258306722074626, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540258306776600578, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540258883803779074, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540258883858305025, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260005012541441, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260005369057281, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260041712701442, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260042383790081, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260522203779074, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260522623209474, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260626897801218, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260627325620225, '', '默', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260797329149953, '', '默认文本', 589, 153, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540260797341732866, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540263777298255873, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540263777306644481, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540264312751493121, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540264382095921154, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540264625428467713, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540264625441050625, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540264845570707457, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540265420802723841, 'string', 'string', 0, 0, 0, 0, 'string', 0, 0, 'string');
INSERT INTO `j_component_text` VALUES (1540265851004096513, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540266221210144770, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540266324046090242, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540266575268122626, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540266840251666434, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540266936485777409, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540267155952734210, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540267369358921729, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540269714511728642, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540269730219397121, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540269765296361474, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540269807386202114, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540269962231517185, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540269973400948737, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540270149721100290, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1540270378277113858, '', '张三', 50, 50, 1000, 500, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1541221098316861441, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1541222209459294210, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1541222837044613122, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1541222960956936193, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1541229188600795138, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1541715256940888066, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1541716861035675650, '', NULL, 50, 50, 100, 100, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1542044515811106818, '', '默认文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1542050337094017026, '', '默认', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1542084025546612738, '', 'E-安保一体化运营平台', 173, 24, 250, 29, '微软雅黑', 22, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1542084025563389953, '', '百度地图', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1542084025622110210, '', '百度一下，你就知道', 61, 51, 150, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1542419392820813826, '', '文本', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1542420409922756609, '', '文字12', 164, 66, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1542426215741267970, '', '默认文本12', 50, 50, 100, 50, '微软雅黑', 16, 0, '0,0,1,1');
INSERT INTO `j_component_text` VALUES (1542691376595083265, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1542694945608048641, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1542694973005242370, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1542717513257852929, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1542718647619952642, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1542718712669413377, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1542719732661231618, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1542799969872912385, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1543101263963627522, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1543154535546101761, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544218866169851905, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544496060892884993, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544496133441761282, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544496176987025409, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544496197673332738, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544496232960012290, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544521168533499905, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544521197797158914, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544521367205097474, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544521517256323073, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544521782986452993, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544522819508023298, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544522866257735682, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544523093182164994, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544523284190769154, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544523409466241025, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1544523549094621186, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547478135015108609, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490421809246210, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490422014767106, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490422031544321, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490605347794946, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490605494595586, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490605821751297, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490660570001410, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490660687441921, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490660741967875, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490673014501378, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490673115164674, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490673127747587, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490710847123457, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490711056838657, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490711363022850, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490721597124610, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490721806839810, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490722129801217, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490725615267841, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490725787234306, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547490726043086849, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547740968986537985, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547740968986537986, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547740969095589889, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741064541171713, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741064717332482, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741065019322369, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741082341797889, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741082476015618, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741082530541570, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741192211591169, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741192236756993, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741192391946242, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741277217550338, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741277372739585, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547741277670535170, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547778693160693762, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547778693160693763, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547778693286522882, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547779996158980098, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547779996335140865, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547779996528078850, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792651393363969, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792651468861441, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792651502415874, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792651544358914, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792651594690561, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792651640827905, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792652181893122, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792652194476034, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792652307722241, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792671857373185, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792671886733313, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792671932870657, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792671991590913, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792672046116866, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792672100642818, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792672599764994, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792672679456770, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792672775925761, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792889738883073, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792889822769153, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792889835352065, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792889843740673, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792889940209666, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792889965375490, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792890506440706, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792890590326786, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547792890674212865, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547825133790621698, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547825133933228034, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547825133970976770, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547825530227847169, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547825530441756673, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547825530802466817, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826686928809985, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826687138525186, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826687289520129, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826953439080450, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826953913036801, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826954441519106, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826965690642434, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826965912940545, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826966487560193, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826974960054273, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826975106854913, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547826975459176449, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547827142841266178, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547827143197782017, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547827144443490306, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547827193508458497, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547827193663647746, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547827194053718017, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547827936550383618, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547827936772681729, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547827936974008322, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547828215916195842, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547828217677803521, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547828217774272514, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547829728617099265, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547829729158164482, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547829730319986689, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547834038176378882, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547834038239293441, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547834038428037121, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547834060922089473, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547834061182136321, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547834061672869890, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547835551217020930, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547835551451901953, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547835551657422850, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547839395468603394, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547839395720261634, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547839395925782530, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840206147874818, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840206315646978, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840206793797634, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840217891926017, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840218089058306, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840218403631105, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840229287849985, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840229472399362, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840229732446209, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840492941799426, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840493197651970, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840494028124161, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840505067532290, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840505252081666, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840505554071554, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840554942001154, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840555223019522, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840555457900545, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840565842997249, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840566082072578, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840566816075778, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840575749943297, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840575972241409, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547840576613969921, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547841621729669122, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547841621842915329, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547841621989715969, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547845485556068354, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547845486243934210, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1547845486273294337, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1549304602199986177, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1549604483875684354, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551771415546580993, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551771415546580994, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551771415588524033, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551771415630467074, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551771415680798722, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551771415697575938, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551771416217669633, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551771416410607617, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551771416410607618, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1551778917172531201, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1552137649807052802, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1552137649823830018, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1552137650046128130, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1552179902084698114, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1552179902323773441, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1552179902357327873, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554031091772444673, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554031091780833282, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554031092212846593, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554359898597363713, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554366299554058241, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554366299591806977, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554366300162232322, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554638149530030081, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554714002565763073, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554714002582540290, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554714002821615618, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554718333834760194, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554718333918646274, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1554718334191276034, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114060452859906, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114060507385858, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114060557717505, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114104979591170, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114105101225985, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114105394827265, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114131059773442, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114131290460161, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114131563089922, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114160658976769, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114160856109058, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114161124544514, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114174881861633, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114175091576833, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114175452286977, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114217563099137, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114217793785858, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114218120941570, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114244599582721, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114244796715009, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114245073539074, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114288333590529, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114288501362689, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114288820129794, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114312513753090, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114312954155009, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114352271560705, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114352456110081, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555114352741322754, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555429602971750402, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);
INSERT INTO `j_component_text` VALUES (1555451988504875009, NULL, NULL, 0, 0, 0, 0, NULL, 0, 0, NULL);

-- ----------------------------
-- Table structure for j_component_video
-- ----------------------------
DROP TABLE IF EXISTS `j_component_video`;
CREATE TABLE `j_component_video`  (
  `video_id` bigint(20) NOT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position_x` int(11) NULL DEFAULT NULL,
  `position_y` int(11) NULL DEFAULT NULL,
  `size_width` int(11) NULL DEFAULT NULL,
  `size_height` int(11) NULL DEFAULT NULL,
  `video_title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `button_type` int(11) NULL DEFAULT NULL,
  `title_picture` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `playback_mode` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`video_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_component_video
-- ----------------------------
INSERT INTO `j_component_video` VALUES (1539548814959558657, '中国', 0, 10, 0, 10, 'string', 0, 'string', 0);
INSERT INTO `j_component_video` VALUES (1542330551925645313, 'string', 0, 0, 0, 0, 'string', 0, 'string', 0);
INSERT INTO `j_component_video` VALUES (1547792652106395651, NULL, 0, 0, 0, 0, NULL, 0, NULL, 0);
INSERT INTO `j_component_video` VALUES (1547792672146780161, NULL, 0, 0, 0, 0, NULL, 0, NULL, 0);
INSERT INTO `j_component_video` VALUES (1547792890024095746, NULL, 0, 0, 0, 0, NULL, 0, NULL, 0);
INSERT INTO `j_component_video` VALUES (1551771416297361410, NULL, 0, 0, 0, 0, NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for j_dict_adjectelation
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_adjectelation`;
CREATE TABLE `j_dict_adjectelation`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NAME` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORDNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SOURCE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CATEGORY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STOPWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SATISFIED` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `AD_WORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1310408763687198722 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_adjectelation
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_category
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_category`;
CREATE TABLE `j_dict_category`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `C_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'C_NAME',
  `FID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'FID',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 433 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_category
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_categorys1
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_categorys1`;
CREATE TABLE `j_dict_categorys1`  (
  `ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `C_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STATUS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCSTATUS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_categorys1
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_changjingci
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_changjingci`;
CREATE TABLE `j_dict_changjingci`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SOURCE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FIRST` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SECOND` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LAIYUAN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CHANGJINGCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORDNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CATEGORY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性级别',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1315930830461394947 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_changjingci
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_duanjufenxi
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_duanjufenxi`;
CREATE TABLE `j_dict_duanjufenxi`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `DUANJUBIAOQIANKUAI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CHETIZHIBIAOFENLEI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CHEXINGZHIBIAOFENLEI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CHETIWORDNAME` bigint(20) NULL DEFAULT NULL,
  `CHEXINGWORDNAME` bigint(20) NULL DEFAULT NULL,
  `ZHUTICISHUXING` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ZHUTICIXIANGGUANCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GUIZE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_duanjufenxi
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_fanyici
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_fanyici`;
CREATE TABLE `j_dict_fanyici`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FANYICI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `XINGRONGCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CATEGORY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性级别',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1315932427765604354 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_fanyici
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_fuci
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_fuci`;
CREATE TABLE `j_dict_fuci`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FUCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SHUXING` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STOPWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SOURCE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1286180953161351171 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_fuci
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_juyifenx
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_juyifenx`;
CREATE TABLE `j_dict_juyifenx`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CAR_CORP` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车企',
  `BRAND` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `AUTOMOTIVE_TYPE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型',
  `QUATITY_OF_SUBJECT_WORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词量',
  `LEVEL_1ST` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级指标',
  `LEVEL_2ND` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级指标',
  `LEVEL_3RD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LEVEL_4TH` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `POST_KIND` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帖子分类',
  `TITTLE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `POST_ID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帖子ID',
  `POST_CONTENTS` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '帖子内容',
  `SUBJECT_WORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词',
  `ADJECTIVE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '形容词',
  `SUBJECT_WORD_STOP_WORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词停用词',
  `REGION` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地域',
  `POST_TIME` datetime NULL DEFAULT NULL COMMENT '发帖时间',
  `SEARCH` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '搜索',
  `SUBJECT_WORD_NOT_NULL` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词不为空',
  `SATISFACTORY` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '满意',
  `NEUTRAL` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中立',
  `UNSATISFACTORY` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '不满意',
  `BOUTIQUE_STICKERS` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '精品贴',
  `QUESTION_ANSWER` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问答站',
  `WORD_OF_MOUTH_STICK` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '口碑贴',
  `VEHICLE_INDEX_TYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型指标分类',
  `EXAM_TIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_juyifenx
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_juyifenxi
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_juyifenxi`;
CREATE TABLE `j_dict_juyifenxi`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `cruser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `crtime` datetime NULL DEFAULT NULL,
  `vehicle_target_classification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车型指标分类',
  `docreltime` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `examtime` datetime NULL DEFAULT NULL,
  `car_corp` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车企',
  `brand` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `automotive_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型',
  `quatity_of_wordname` bigint(255) NULL DEFAULT NULL COMMENT '主题词量',
  `level_1st` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级指标',
  `level_2nd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级指标',
  `level_3rd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '三级指标',
  `level_4th` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '四级指标',
  `post_kind` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帖子分类',
  `tittle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `post_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帖子ID',
  `post_contents` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帖子内容',
  `subject_word` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词',
  `adjective` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '形容词',
  `subject_word_stop_word` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词停用词',
  `region` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地域',
  `post_time` datetime NULL DEFAULT NULL COMMENT '发帖时间',
  `search` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '搜索',
  `subject_word_not_null` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词不为空',
  `satisfactory` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '满意',
  `neutral` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中立',
  `unsatisfactory` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '不满意',
  `boutique_stickers` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '精品贴',
  `question_answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问答站',
  `word_of_mouth_stick` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '口碑贴',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_juyifenxi
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_pause_se
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_pause_se`;
CREATE TABLE `j_dict_pause_se`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PAUSE_SENTENCE_TAG` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '断句标签块',
  `BODYWORK_INDEX_TYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车体指标分类',
  `VEHICLE_INDEX_TYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型指标分类',
  `QUATITY_OF_SUBJECT_WORD` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词(个)',
  `SPORT_VEHICLE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运动型多用途车',
  `QUATITY_OF_BODYWORK_SUBJECT_WORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车体主题词',
  `QUATITY_OF_VEHICLE_SUBJECT_WORD` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型主题词',
  `SUBJECT_WORD_ATTRIBUTIVE_WORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词属性词',
  `SCENE_WORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '场景词',
  `ADJECTIVE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '形容词',
  `SUBJECT_WORD_STOP_WORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词停用词',
  `SATISFACTION` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性级别',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_pause_se
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_pause_sentence_other
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_pause_sentence_other`;
CREATE TABLE `j_dict_pause_sentence_other`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SERIAL_NUMBER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  `CONTENT` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `OPERATE_CONTENT` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `PAUSES_SENTENCE_TIME` datetime NULL DEFAULT NULL COMMENT '断句时间',
  `OPERATING_MODE` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作方式',
  `OPERATING_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户',
  `PAUSES_SENTENCE_ADVICE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '断句建议',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_pause_sentence_other
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_pingji
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_pingji`;
CREATE TABLE `j_dict_pingji`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JIBIE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FENSHU` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SATISFACTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1286180979749044227 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_pingji
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_post_contents
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_post_contents`;
CREATE TABLE `j_dict_post_contents`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `AUTOMOTIVE_TYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型',
  `POST_TIME` datetime NULL DEFAULT NULL COMMENT '发帖时间',
  `EXAM_TIME` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `POST_LORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帖主',
  `REGION` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地域',
  `ORIGINAL_WIBE_SITE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原网址',
  `URL` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网址详情',
  `VEHICLE_INDEX_TYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型指标分类',
  `SUBJECT_WORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词',
  `ADJECTIVE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '形容词',
  `SUBJECT_WORD_STOP_WORD` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词停用词',
  `SATISFACTORY` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '满意',
  `NEUTRAL` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中立',
  `UNSATISFACTORY` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '不满意',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_post_contents
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_post_sentense
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_post_sentense`;
CREATE TABLE `j_dict_post_sentense`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BODYWORK_INDEX_TYPE` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车体指标分类',
  `VEHICLE_INDEX_TYPE` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型指标分类',
  `QUATITY_OF_SUBJECT_WORD` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词',
  `SPORT_VEHICLE` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运动型多用途车',
  `QUATITY_OF_BODYWORK_SUBJECT_WORD` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车体主题词',
  `QUATITY_OF_VEHICLE_SUBJECT_WORD` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车型主题词',
  `SUBJECT_WORD_ATTRIBUTIVE_WORD` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词属性词',
  `SCENE_WORD` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '场景词',
  `ADJECTIVE` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '形容词',
  `SUBJECT_WORD_STOP_WORD` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词停用词',
  `SATISFACTION` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性级别',
  `POST_ID` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '贴子ID',
  `JUYIFENX_ID` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '句义分析ID',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_post_sentense
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_shuxingci
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_shuxingci`;
CREATE TABLE `j_dict_shuxingci`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SHUXINGCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORDNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RELATIONWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SX_RELATIONWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TONGYICI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PINYIN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TONGYINCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RANK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1315930561224826882 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_shuxingci
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_shuxingjibie
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_shuxingjibie`;
CREATE TABLE `j_dict_shuxingjibie`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `SHUXINGJIBIE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MANYIDU` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `CATEGORY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1303690948959682580 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_shuxingjibie
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_stopword
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_stopword`;
CREATE TABLE `j_dict_stopword`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SOURCE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FIRST` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SECOND` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LAIYUAN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STOPWORDNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORDNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1315931105364467714 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_stopword
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_stopword1
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_stopword1`;
CREATE TABLE `j_dict_stopword1`  (
  `ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STATUS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STOPWORDNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCSTATUS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SOURCE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FIRST` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SECOND` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LAIYUAN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_stopword1
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_word
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_word`;
CREATE TABLE `j_dict_word`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '25',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NAME` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORDNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CATEGORY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SOURCE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FILTE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEARCH_ORDER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RECOMMEND` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STOPWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CXFL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CTFL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TONGYICI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ENTONGYICI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SUOXIE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PINYIN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TONGYINCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NEWTONGYINCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ISSTOP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FILE_PATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `FILE_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `FILE_TIME` datetime NULL DEFAULT NULL COMMENT '文件上传时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1315929951142338562 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_word
-- ----------------------------

-- ----------------------------
-- Table structure for j_dict_wordsrelation
-- ----------------------------
DROP TABLE IF EXISTS `j_dict_wordsrelation`;
CREATE TABLE `j_dict_wordsrelation`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NAME` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORDNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TONGYICI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `HOMOIONYM` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RELATEDWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UPPERWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `INFERIORWORDS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PINYINWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ENGLISHWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MARK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SPELL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CXFL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CTFL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CATEGORY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CT_ADJ` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RULE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `REMARKS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SHUXING` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TONGYINCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RELPINYIN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `RELTONGYINCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SXTONGYINCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SXTONGYICI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SXPINYIN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CTFL_SANJI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ZBFENLEI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `T1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `T2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLAG` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TINGYONGCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CHANGJINGCI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ZTTONGYICI` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1315948829817888771 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dict_wordsrelation
-- ----------------------------

-- ----------------------------
-- Table structure for j_dbsource
-- ----------------------------
DROP TABLE IF EXISTS `j_dbsource`;
CREATE TABLE `j_dbsource`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL,
  `STATUS` smallint(6) NULL DEFAULT 0,
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `IP` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `PORT` bigint(10) NULL DEFAULT NULL COMMENT '端口',
  `ACOUNT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `PW` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `TYPE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源类型'
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'dbSource' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_dbsource
-- ----------------------------
INSERT INTO `j_dbsource` VALUES (1, NULL, NULL, NULL, 0, 1167324599977414658, 'admin', NULL, '2022-12-07 17:35:53', '本地测试', '127.0.0.1', 9200, NULL, '');

-- ----------------------------
-- Table structure for j_fenfa
-- ----------------------------
DROP TABLE IF EXISTS `j_fenfa`;
CREATE TABLE `j_fenfa`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `COLUMNNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目名称',
  `FROMIDS` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '同步栏目',
  `FROMNAMES` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '同步栏目名称',
  `STARTTIME` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `ENDTIME` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `SYNCWHILE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步时机(0:新建时,1:修改时,2:发布后同步并且不发布,3:发布后同步且发布)',
  `SQLSTR` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'sql',
  `SYNCTYPE` bigint(3) NULL DEFAULT NULL COMMENT '同步模式(0:复制,1:引用,2:镜像)',
  `FILETIME` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分发' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_fenfa
-- ----------------------------

-- ----------------------------
-- Table structure for j_file
-- ----------------------------
DROP TABLE IF EXISTS `j_file`;
CREATE TABLE `j_file`  (
  `ID` bigint(20) NOT NULL COMMENT '主键id',
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `FSIZE` double NULL DEFAULT NULL COMMENT '文件大小',
  `FORM` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件格式',
  `DATETIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '上传时间',
  `PATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径',
  `RESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件来源',
  `COPYRIGHT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件版权',
  `FDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件描述',
  `SCALE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件比例',
  `RESO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件分辨率',
  `STYLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件板式',
  `TYPE` int(2) NULL DEFAULT NULL COMMENT '文件分类',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `CORPORATION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '制片公司单位',
  `CODESTYLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码格式',
  `RADIO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '音频声道',
  `SUPERVISOR` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '监制',
  `PRODUCER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '制片',
  `PLANNER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '策划',
  `WRITER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编导，作者',
  `CAMERAMAN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摄像',
  `WORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字幕',
  `MUSIC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配乐',
  `SIGN` int(2) NULL DEFAULT NULL COMMENT '上传标识（图片为0，视频为1，文件为2，音频为3）',
  `KEYWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关键词',
  `CUSTOM_KEYWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义关键词',
  `FREFERENCES` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参考文献',
  `LENGTH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '音频时长',
  `CONTENTTYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容类型',
  `COLUMNID` bigint(20) NULL DEFAULT NULL COMMENT '栏目id',
  `OTHERFORM` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其他格式',
  `OTHERCP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其他版权',
  `ISNEW` int(10) NULL DEFAULT NULL COMMENT '是否为修改后的图片,列表只显示为1的图片,原图为0',
  `WMKID` bigint(20) NULL DEFAULT NULL COMMENT '对应设置id',
  `URL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问路径',
  `PICSIGN` int(10) NULL DEFAULT NULL COMMENT '横竖图区分,0横图1竖图',
  `PARENTID` bigint(20) NULL DEFAULT NULL COMMENT '父栏目ID',
  `SITEID` bigint(20) NULL DEFAULT NULL COMMENT '站点id',
  `COMPANYID` bigint(20) NULL DEFAULT NULL COMMENT '机构id',
  `STATUS` int(10) NULL DEFAULT NULL COMMENT '状态标志，0新建，10待审核，20发布',
  `RESOTYPE` int(20) NULL DEFAULT NULL COMMENT '对应清晰度',
  `COLLECTION` int(10) NULL DEFAULT NULL COMMENT '1收藏，0未收藏',
  `RESPARENTID` bigint(20) NULL DEFAULT NULL COMMENT '源视频为0，源文件为0',
  `PDFSIGN` int(10) NULL DEFAULT NULL COMMENT '0为没加水印，1加水印，200表示文件已转过pdf，100表示文件已加过水印',
  `ISPROCESSED` int(10) NULL DEFAULT NULL COMMENT '资源是否已处理',
  `PDFURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'pdfurl',
  `CUTPICURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频截图url',
  `VIDEOURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频加水印url',
  `H264URL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频H264url',
  `MP3URL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'mp3url',
  `NEWVIDEOID` bigint(20) NULL DEFAULT NULL COMMENT '新视频id',
  `COVER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频封面',
  `PROCESSING` int(10) NULL DEFAULT NULL COMMENT '资源处理标识，0为未处理的，1为正在处理，2为处理过的资源',
  `DATAID` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WIDTH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宽度',
  `HEIGHT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '高度',
  `THEME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题词',
  `TYPENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类',
  `LOCATION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拍摄地点',
  `PHOTOTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '拍摄时间',
  `PROTYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目附件类型',
  `REGION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区',
  `SHOTMAN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拍摄者',
  `INTODATABASE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库时间',
  `INHERRITORPRO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属项目',
  `INHERITOR` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属传承人',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `IMGINFO` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片信息',
  `TABLEID` bigint(20) NULL DEFAULT NULL,
  `TAKE_TIME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拍摄时间',
  `LONGITUDE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经度',
  `LATITUDE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `resourceorder` bigint(20) NULL DEFAULT NULL COMMENT '排序号',
  `SAMPLERATE` int(10) NULL DEFAULT NULL COMMENT '采样率',
  `AUDIONUM` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '声道数',
  `BITRATE` int(10) NULL DEFAULT NULL COMMENT '比特率',
  `CPSPATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图路径',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_file
-- ----------------------------
INSERT INTO `j_file` VALUES (1601101886283849730, '1 (1)', 119.12, 'jpg', '2022-12-09 14:30:11', '\\opt\\fastdev\\upload\\webpic\\2bb86082-fdac-4d51-aace-c3e4709a675b.jpg', '默认来源', '默认版权', NULL, '4:3', '1024*768', NULL, NULL, 'admin', 1, '2022-12-09 14:30:11', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '委办', '', NULL, NULL, NULL, 1601101725998522370, NULL, '其他', 1, NULL, '/files/webpic/2bb86082-fdac-4d51-aace-c3e4709a675b.jpg', 0, NULL, 1601101087986163714, 1167323268476895233, 0, NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1024', '768', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '{}', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for j_group_business
-- ----------------------------
DROP TABLE IF EXISTS `j_group_business`;
CREATE TABLE `j_group_business`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(18) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(18) NULL DEFAULT NULL,
  `SITEID` bigint(18) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_group_business
-- ----------------------------

-- ----------------------------
-- Table structure for j_log
-- ----------------------------
DROP TABLE IF EXISTS `j_log`;
CREATE TABLE `j_log`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(20) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(20) NULL DEFAULT NULL,
  `SITEID` bigint(20) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` smallint(6) NULL DEFAULT 0,
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `HANDLETITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作标题',
  `HANDLETYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型（增加/修改/删除/浏览/授权/登录、登出/锁定/解锁等）',
  `USERID` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `USERNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `TRUENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `MOBILE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `IPSTR` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `TARGETID` bigint(20) NULL DEFAULT NULL COMMENT '操作对象id',
  `TARGETTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作对象标题',
  `TARGETTYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作对象类型',
  `HANDLEDATE` datetime NULL DEFAULT NULL COMMENT '源服务器时间',
  `TARGETDATE` datetime NULL DEFAULT NULL COMMENT '写服务器时间',
  `RESULT` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果',
  `OBJJSON` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作后对象信息',
  `ZHAIYAO` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志摘要',
  `REMARKS` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1601118061998919683 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for j_module_field
-- ----------------------------
DROP TABLE IF EXISTS `j_module_field`;
CREATE TABLE `j_module_field`  (
  `relation_id` bigint(20) NOT NULL,
  `module_id` bigint(20) NULL DEFAULT NULL COMMENT '组件id',
  `module_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件类型，具体见枚举类ModuleType',
  `field_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据字段id',
  `field_order` int(11) NULL DEFAULT NULL COMMENT '字段排序(用于关联多个字段的组件，比如表格组件)',
  `show_width` int(11) NULL DEFAULT NULL COMMENT '字段显示宽度（用于表格组件等）',
  PRIMARY KEY (`relation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_module_field
-- ----------------------------
INSERT INTO `j_module_field` VALUES (1594897817435045889, 1593505895109775362, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594897817586040833, 1593505895109775362, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594898459239055362, 1594898459130003457, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594898459314552833, 1594898459130003457, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594901660394831873, 1594901660336111617, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594901660482912258, 1594901660336111617, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594929778186686465, 1594929778132160514, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594929778224435201, 1594929778132160514, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594929921417973761, 1594929921367642113, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594929921459916802, 1594929921367642113, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594940442460459009, 1594937263970750466, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594950722137526274, 1594930354907680770, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594950722179469314, 1594930354907680770, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594953882310508545, 1594953882117570562, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594953882700578818, 1594953882117570562, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594953882897711105, 1594953882117570562, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594972872571285505, 1594972872437067778, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594972872701308930, 1594972872437067778, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594974452330958850, 1594974452117049346, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594974452377096194, 1594974452117049346, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594975096601219074, 1594975096467001346, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594975096651550721, 1594975096467001346, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594976499159699458, 1594976498945789954, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1594976499197448193, 1594976498945789954, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595239799235493889, 1595239799038361602, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595239799424237570, 1595239799038361602, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595239799558455297, 1595239799038361602, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595255701599371266, 1595255701272215554, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595255701729394690, 1595255701272215554, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595310446171279362, 1595310445995118594, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595310446242582529, 1595310445995118594, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595310619026935809, 1595310618796249089, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595310619073073153, 1595310618796249089, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595311584748654593, 1595311584702517250, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595311584794791937, 1595311584702517250, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595329412340011010, 1595329412251930625, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595329412386148353, 1595329412251930625, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595329412432285697, 1595329412251930625, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595333035337863170, 1595331656200376322, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595333035379806209, 1595331656200376322, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595333344072192001, 1595333344013471746, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595333344105746433, 1595333344013471746, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595333399634137090, 1595333399520890881, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595333399676080129, 1595333399520890881, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1595333399713828865, 1595333399520890881, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596380925434941442, 1594985372746682369, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596381225969405954, 1595329551599292417, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596381226011348994, 1595329551599292417, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596381226044903426, 1595329551599292417, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596385786662641666, 1595318913804161025, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596385786708779010, 1595318913804161025, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596386092905553922, 1594977907166580737, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596386092955885569, 1594977907166580737, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596388072306663426, 1596388072252137474, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596391578421846018, 1596387776453042178, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1596391578455400449, 1596387776453042178, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1597478396361859074, 1597478396173115394, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1597478396563185666, 1597478396173115394, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_module_field` VALUES (1597478396638683138, 1597478396173115394, 'table', '1546384590879211522', NULL, NULL);

-- ----------------------------
-- Table structure for j_module_search
-- ----------------------------
DROP TABLE IF EXISTS `j_module_search`;
CREATE TABLE `j_module_search`  (
  `search_id` bigint(20) NOT NULL,
  `db_table_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联的元数据表id',
  `search_relation` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '搜索关系，关联分类法',
  `visual_template_id` bigint(20) NULL DEFAULT NULL COMMENT '可视化模板id',
  PRIMARY KEY (`search_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_module_search
-- ----------------------------
INSERT INTO `j_module_search` VALUES (1594985372746682369, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1596388072252137474, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1597518038478077953, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1597519320236408834, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1598145650602778626, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1598156394253754369, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1598156688127664130, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1598156964221919234, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1598159704083898370, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1598161145695543297, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1598162255642599426, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1598162629103427586, '1537615362311454721', NULL, NULL);
INSERT INTO `j_module_search` VALUES (1598248439240708097, '1537615362311454721', NULL, 220);
INSERT INTO `j_module_search` VALUES (1598249947592118274, '1537615362311454721', NULL, 223);
INSERT INTO `j_module_search` VALUES (1598564277286064129, '1537615362311454721', NULL, 224);
INSERT INTO `j_module_search` VALUES (1599936575524040705, '1537615362311454721', NULL, 225);
INSERT INTO `j_module_search` VALUES (1599941353247023106, '1537615362311454721', NULL, 226);
INSERT INTO `j_module_search` VALUES (1599946769884454914, '1537615362311454721', NULL, 227);
INSERT INTO `j_module_search` VALUES (1599948779560050690, '1537615362311454721', NULL, 227);
INSERT INTO `j_module_search` VALUES (1600024900075143170, '1537615362311454721', NULL, 232);
INSERT INTO `j_module_search` VALUES (1600025648267673601, '1537615362311454721', NULL, 232);
INSERT INTO `j_module_search` VALUES (1600739149965635585, '1537615362311454721', NULL, 232);
INSERT INTO `j_module_search` VALUES (1601027648505720834, '1537615362311454721', NULL, 245);
INSERT INTO `j_module_search` VALUES (1601028321938976769, '1537615362311454721', NULL, 245);

-- ----------------------------
-- Table structure for j_module_table
-- ----------------------------
DROP TABLE IF EXISTS `j_module_table`;
CREATE TABLE `j_module_table`  (
  `table_id` bigint(20) NOT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属菜单id',
  `table_style` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表格样式，关联分类法',
  `position_x` int(11) NULL DEFAULT NULL COMMENT '位置X',
  `position_y` int(11) NULL DEFAULT NULL COMMENT '位置Y',
  `size_width` int(11) NULL DEFAULT NULL COMMENT '高度',
  `size_height` int(11) NULL DEFAULT NULL COMMENT '宽度',
  `table_font` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字体，关联分类法',
  `margin_left` int(11) NULL DEFAULT NULL COMMENT '边距左',
  `margin_top` int(11) NULL DEFAULT NULL COMMENT '边距上',
  `margin_right` int(11) NULL DEFAULT NULL COMMENT '边距右',
  `margin_bottom` int(11) NULL DEFAULT NULL COMMENT '边距下',
  `type_setup` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排版，1-左对齐，2-居中，3-右对齐',
  `select_setup` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选择设置，关联分类法',
  `page_setup` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分页设置，关联分类法',
  `page_size` int(11) NULL DEFAULT NULL COMMENT '每页显示数据条数',
  `db_table_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联的元数据表id',
  `visual_template_id` bigint(20) NULL DEFAULT NULL COMMENT '可视化模板id',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_module_table
-- ----------------------------
INSERT INTO `j_module_table` VALUES (1592420388439207937, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1592427338149072897, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1592767361725964289, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 20, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1592772370307948545, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1534461826629001217', NULL);
INSERT INTO `j_module_table` VALUES (1592772461865410561, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1534461826629001217', NULL);
INSERT INTO `j_module_table` VALUES (1593046819229855745, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1534461826629001217', NULL);
INSERT INTO `j_module_table` VALUES (1593049290132090882, NULL, NULL, NULL, NULL, NULL, NULL, '黑体', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `j_module_table` VALUES (1593124345235042305, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `j_module_table` VALUES (1593134480380588034, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `j_module_table` VALUES (1593135245551661058, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1534461826629001217', NULL);
INSERT INTO `j_module_table` VALUES (1593136513749807105, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `j_module_table` VALUES (1593137003720011777, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `j_module_table` VALUES (1593137502456311810, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593139525708869634, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593153513918230530, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593164746264342530, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593166232138477570, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593166660636962818, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593428678602272770, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593434537256275969, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593439465928544258, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1534461826629001217', NULL);
INSERT INTO `j_module_table` VALUES (1593439957341589505, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593440044075601921, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593501675568648194, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 10, '1534461826629001217', NULL);
INSERT INTO `j_module_table` VALUES (1593505134585991170, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593505895109775362, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593506724428345345, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593506766749061122, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593508879650672641, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593510302836740098, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593514643568087042, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593516616216178690, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593847778981896193, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593852908804464641, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593857255537778689, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593857743893176321, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1593868110664966146, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594516765659090946, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594517319944753154, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594596599550603265, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594622099891097602, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594624999535345666, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594625256591654913, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594626773230751746, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594627033139445762, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594627883152642049, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594628717945225218, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594629418423095297, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594629470130733058, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594630495428345858, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594638662555865089, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594892961718665217, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594893386412916737, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594894562558672898, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594895001521946625, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594898459130003457, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594901660336111617, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594929778132160514, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594929921367642113, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594930354907680770, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594937263970750466, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594953882117570562, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594972872437067778, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594974452117049346, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594975096467001346, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594976498945789954, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1594977907166580737, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 7, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595239799038361602, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595255701272215554, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 6, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595310445995118594, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595310618796249089, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595311584702517250, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595311675966377985, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 15, '1534461826629001217', NULL);
INSERT INTO `j_module_table` VALUES (1595318913804161025, NULL, NULL, NULL, NULL, NULL, NULL, '宋体', NULL, NULL, NULL, NULL, NULL, NULL, '1', 6, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595329412251930625, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595329551599292417, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595331656200376322, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 13, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595333344013471746, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595333399520890881, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 12, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1595652013767675905, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1534461826629001217', NULL);
INSERT INTO `j_module_table` VALUES (1595680224278892546, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1534461826629001217', NULL);
INSERT INTO `j_module_table` VALUES (1596387776453042178, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 8, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1597478396173115394, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', NULL);
INSERT INTO `j_module_table` VALUES (1597482720974311426, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 207);
INSERT INTO `j_module_table` VALUES (1597490418126041090, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 208);
INSERT INTO `j_module_table` VALUES (1597518817141919745, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 209);
INSERT INTO `j_module_table` VALUES (1597519830624485378, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 5, '1537615362311454721', 210);
INSERT INTO `j_module_table` VALUES (1597522605248856066, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 212);
INSERT INTO `j_module_table` VALUES (1597526512700203010, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 210);
INSERT INTO `j_module_table` VALUES (1597526911154888705, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 6, '1537615362311454721', 213);
INSERT INTO `j_module_table` VALUES (1597528476355895297, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 213);
INSERT INTO `j_module_table` VALUES (1597531890204778498, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 214);
INSERT INTO `j_module_table` VALUES (1597533016312487938, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 5, '1537615362311454721', 215);
INSERT INTO `j_module_table` VALUES (1597777042911227905, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 8, '1537615362311454721', 216);
INSERT INTO `j_module_table` VALUES (1598202029728059394, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 217);
INSERT INTO `j_module_table` VALUES (1598209424621801474, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 218);
INSERT INTO `j_module_table` VALUES (1598213119983996929, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1534461826629001217', 219);
INSERT INTO `j_module_table` VALUES (1598213210488688641, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 219);
INSERT INTO `j_module_table` VALUES (1598214504242728961, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 220);
INSERT INTO `j_module_table` VALUES (1598239386410143746, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 221);
INSERT INTO `j_module_table` VALUES (1598565731614838785, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 224);
INSERT INTO `j_module_table` VALUES (1599936519202926594, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 8, '1537615362311454721', 225);
INSERT INTO `j_module_table` VALUES (1599941309659815938, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 6, '1537615362311454721', 226);
INSERT INTO `j_module_table` VALUES (1599946719326314498, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 8, '1537615362311454721', 227);
INSERT INTO `j_module_table` VALUES (1600304877169537025, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 232);
INSERT INTO `j_module_table` VALUES (1600739206408384513, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 232);
INSERT INTO `j_module_table` VALUES (1601028468878028801, NULL, NULL, NULL, NULL, NULL, NULL, '微软雅黑', NULL, NULL, NULL, NULL, NULL, NULL, '1', 10, '1537615362311454721', 245);

-- ----------------------------
-- Table structure for j_permission
-- ----------------------------
DROP TABLE IF EXISTS `j_permission`;
CREATE TABLE `j_permission`  (
  `ID` bigint(20) NOT NULL COMMENT '主键id',
  `OWNER_TYPE` int(5) NULL DEFAULT NULL COMMENT '对象类型(0: 系统用户,1角色, 2组织, 3：互联网用户)',
  `OWNER_ID` bigint(20) NULL DEFAULT NULL COMMENT '所属对象编号',
  `ISURL` int(2) NULL DEFAULT NULL COMMENT '授权方式是否是url(1:是)',
  `PERMISSION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限值',
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '授权关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_permission
-- ----------------------------
INSERT INTO `j_permission` VALUES (1, 1, 1, NULL, 'metadata', NULL, NULL, NULL, '2022-12-09 06:11:20', NULL, '2022-12-09 06:11:20', NULL);
INSERT INTO `j_permission` VALUES (2, 1, 1, NULL, 'menu', NULL, NULL, NULL, '2022-12-09 06:11:20', NULL, '2022-12-09 06:11:20', NULL);
INSERT INTO `j_permission` VALUES (3, 1, 1, NULL, 'group', NULL, NULL, NULL, '2022-12-09 06:11:20', NULL, '2022-12-09 06:11:20', NULL);
INSERT INTO `j_permission` VALUES (4, 1, 1, NULL, 'column', NULL, NULL, NULL, '2022-12-09 06:11:20', NULL, '2022-12-09 06:11:20', NULL);
INSERT INTO `j_permission` VALUES (5, 1, 1, NULL, 'company', NULL, NULL, NULL, '2022-12-09 06:11:20', NULL, '2022-12-09 06:11:20', NULL);
INSERT INTO `j_permission` VALUES (6, 1, 1, NULL, 'site', NULL, NULL, NULL, '2022-12-09 06:11:21', NULL, '2022-12-09 06:11:21', NULL);
INSERT INTO `j_permission` VALUES (1601099772644040706, 1, 10, 0, 'menu:view:344', NULL, 'admin', 1, '2022-12-09 14:21:47', NULL, NULL, NULL);
INSERT INTO `j_permission` VALUES (1601099772845367297, 1, 10, 0, 'menu:view:345', NULL, 'admin', 1, '2022-12-09 14:21:48', NULL, NULL, NULL);
INSERT INTO `j_permission` VALUES (1601099772845367298, 1, 10, 0, 'menu:view:346', NULL, 'admin', 1, '2022-12-09 14:21:48', NULL, NULL, NULL);
INSERT INTO `j_permission` VALUES (1601099772845367299, 1, 10, 0, 'menu:view:348', NULL, 'admin', 1, '2022-12-09 14:21:48', NULL, NULL, NULL);
INSERT INTO `j_permission` VALUES (1601099772845367300, 1, 10, 0, 'menu:view:349', NULL, 'admin', 1, '2022-12-09 14:21:48', NULL, NULL, NULL);
INSERT INTO `j_permission` VALUES (1601099772845367301, 1, 10, 0, 'menu:view:350', NULL, 'admin', 1, '2022-12-09 14:21:48', NULL, NULL, NULL);
INSERT INTO `j_permission` VALUES (1601099772845367302, 1, 10, 0, 'menu:view:351', NULL, 'admin', 1, '2022-12-09 14:21:48', NULL, NULL, NULL);
INSERT INTO `j_permission` VALUES (1601099772845367303, 1, 10, 0, 'menu:view:352', NULL, 'admin', 1, '2022-12-09 14:21:48', NULL, NULL, NULL);
INSERT INTO `j_permission` VALUES (1601099772908281858, 1, 10, 0, 'menu:view:1247836832264114178', NULL, 'admin', 1, '2022-12-09 14:21:48', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for j_relation_module_field
-- ----------------------------
DROP TABLE IF EXISTS `j_relation_module_field`;
CREATE TABLE `j_relation_module_field`  (
  `relation_id` bigint(20) NOT NULL,
  `module_id` bigint(20) NULL DEFAULT NULL COMMENT '组件id',
  `module_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件类型编码',
  `field_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据字段id',
  `field_order` int(11) NULL DEFAULT NULL COMMENT '字段排序(用于关联多个字段的组件，比如表格组件)',
  `show_width` int(11) NULL DEFAULT NULL COMMENT '字段显示宽度（用于表格组件等）',
  PRIMARY KEY (`relation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_relation_module_field
-- ----------------------------
INSERT INTO `j_relation_module_field` VALUES (1597482721506988034, 1597482720974311426, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597482721704120322, 1597482720974311426, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597482721901252609, 1597482720974311426, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597487701919985665, 1597487701668327425, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597487702234558465, 1597487701668327425, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597487702486216706, 1597487701668327425, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597489014405787650, 1597489004645642241, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597489014909104130, 1597489004645642241, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597489015412420609, 1597489004645642241, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597489292983070721, 1597489261580316674, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597489293171814401, 1597489261580316674, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597489293360558081, 1597489261580316674, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597490418490945537, 1597490418126041090, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597490418742603778, 1597490418126041090, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597490418994262018, 1597490418126041090, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597518038645850113, 1597518038478077953, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597518038754902017, 1597518038478077953, 'search', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597518817733316610, 1597518817141919745, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597518818190495746, 1597518817141919745, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597522605341130754, 1597522605248856066, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597522605429211137, 1597522605248856066, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597526512796672002, 1597526512700203010, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597528476435587073, 1597528476355895297, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597528476594970625, 1597528476355895297, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597528996579614721, 1597526911154888705, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597528996634140674, 1597526911154888705, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597528996671889410, 1597526911154888705, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597532079128813570, 1597531890204778498, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597532079162368001, 1597531890204778498, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597777195646808065, 1597777042911227905, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1597777195772637185, 1597777042911227905, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598141684489887745, 1597533016312487938, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598141684527636482, 1597533016312487938, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598145650711830530, 1598145650602778626, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598156394379583490, 1598156394253754369, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598156688173801473, 1598156688127664130, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598156964268056578, 1598156964221919234, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598159704255864834, 1598159704083898370, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598161145792012290, 1598161145695543297, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598162255751651330, 1598162255642599426, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598165736545820673, 1598162629103427586, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598199320891596801, 1597519320236408834, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598202032924119041, 1598202029728059394, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598202033448407042, 1598202029728059394, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598209424739241986, 1598209424621801474, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598209424776990722, 1598209424621801474, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598209424814739458, 1598209424621801474, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598213210597740546, 1598213210488688641, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598213210685820930, 1598213210488688641, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598213210719375361, 1598213210488688641, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598216587503194114, 1597519830624485378, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598216587540942849, 1597519830624485378, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598218465720913922, 1598214504242728961, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598218465775439873, 1598214504242728961, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598239386548555778, 1598239386410143746, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598239386590498817, 1598239386410143746, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598248439446228993, 1598248439240708097, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1598249947642449922, 1598249947592118274, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599936519286812673, 1599936519202926594, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599936519328755714, 1599936519202926594, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599936519362310145, 1599936519202926594, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599936894492471297, 1599936575524040705, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599936894526025730, 1599936575524040705, 'search', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599941309986971650, 1599941309659815938, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599941310309933057, 1599941309659815938, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599941310435762178, 1599941309659815938, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599941353444155394, 1599941353247023106, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599946770408742913, 1599946769884454914, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599956321392914433, 1598565731614838785, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1599956321434857473, 1598565731614838785, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600015504670392321, 1599946719326314498, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600015504796221441, 1599946719326314498, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600015504930439169, 1599946719326314498, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600024900171612162, 1600024900075143170, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600025648477388802, 1600025648267673601, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600025648515137538, 1600025648267673601, 'search', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600026585283362817, 1599948779560050690, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600065279281471489, 1598564277286064129, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600304877219868673, 1600304877169537025, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600304877253423106, 1600304877169537025, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600304877291171841, 1600304877169537025, 'table', '1546384590879211522', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600739150020161537, 1600739149965635585, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600739534398763010, 1600739206408384513, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1600739534486843393, 1600739206408384513, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1601027648677687297, 1601027648505720834, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1601044498190905345, 1601028321938976769, 'search', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1601044498480312322, 1601028321938976769, 'search', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1601045084118396929, 1601028468878028801, 'table', '1537626884043571201', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1601045084177117186, 1601028468878028801, 'table', '1546384102892912642', NULL, NULL);
INSERT INTO `j_relation_module_field` VALUES (1601045084214865921, 1601028468878028801, 'table', '1546384590879211522', NULL, NULL);

-- ----------------------------
-- Table structure for j_relation_module_template
-- ----------------------------
DROP TABLE IF EXISTS `j_relation_module_template`;
CREATE TABLE `j_relation_module_template`  (
  `relation_id` bigint(20) NOT NULL,
  `module_id` bigint(20) NULL DEFAULT NULL COMMENT '组件id',
  `module_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件类型编码',
  `visual_template_id` bigint(20) NULL DEFAULT NULL COMMENT '可视化模板id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`relation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_relation_module_template
-- ----------------------------
INSERT INTO `j_relation_module_template` VALUES (1597490457317617666, 1597490418126041090, 'table', 208, '2022-11-29 15:19:39');
INSERT INTO `j_relation_module_template` VALUES (1597518818643480578, 1597518817141919745, 'table', 209, '2022-11-29 17:12:21');
INSERT INTO `j_relation_module_template` VALUES (1597519830746120194, 1597519830624485378, 'table', 210, '2022-11-29 17:16:23');
INSERT INTO `j_relation_module_template` VALUES (1597522605475348481, 1597522605248856066, 'table', 212, '2022-11-29 17:27:24');
INSERT INTO `j_relation_module_template` VALUES (1597526512830226434, 1597526512700203010, 'table', 210, '2022-11-29 17:42:56');
INSERT INTO `j_relation_module_template` VALUES (1597526911419129857, 1597526911154888705, 'table', 213, '2022-11-29 17:44:31');
INSERT INTO `j_relation_module_template` VALUES (1597528476758548481, 1597528476355895297, 'table', 213, '2022-11-29 17:50:44');
INSERT INTO `j_relation_module_template` VALUES (1597531890359967746, 1597531890204778498, 'table', 214, '2022-11-29 18:04:18');
INSERT INTO `j_relation_module_template` VALUES (1597533016446705665, 1597533016312487938, 'table', 215, '2022-11-29 18:08:47');
INSERT INTO `j_relation_module_template` VALUES (1597777043636842497, 1597777042911227905, 'table', 216, '2022-11-30 10:18:27');
INSERT INTO `j_relation_module_template` VALUES (1598202033775562754, 1598202029728059394, 'table', 217, '2022-12-01 14:27:13');
INSERT INTO `j_relation_module_template` VALUES (1598209424852488194, 1598209424621801474, 'table', 218, '2022-12-01 14:56:35');
INSERT INTO `j_relation_module_template` VALUES (1598213120105631746, 1598213119983996929, 'table', 219, '2022-12-01 15:11:16');
INSERT INTO `j_relation_module_template` VALUES (1598213210769707009, 1598213210488688641, 'table', 219, '2022-12-01 15:11:37');
INSERT INTO `j_relation_module_template` VALUES (1598214504506970114, 1598214504242728961, 'table', 220, '2022-12-01 15:16:46');
INSERT INTO `j_relation_module_template` VALUES (1598239386624053250, 1598239386410143746, 'table', 221, '2022-12-01 16:55:38');
INSERT INTO `j_relation_module_template` VALUES (1598248439488172033, 1598248439240708097, 'search', 220, '2022-12-01 17:31:37');
INSERT INTO `j_relation_module_template` VALUES (1598249947680198657, 1598249947592118274, 'search', 223, '2022-12-01 17:37:36');
INSERT INTO `j_relation_module_template` VALUES (1598564277474807809, 1598564277286064129, 'search', 224, '2022-12-02 14:26:38');
INSERT INTO `j_relation_module_template` VALUES (1598565731778416641, 1598565731614838785, 'table', 224, '2022-12-02 14:32:25');
INSERT INTO `j_relation_module_template` VALUES (1599936519400058881, 1599936519202926594, 'table', 225, '2022-12-06 09:19:26');
INSERT INTO `j_relation_module_template` VALUES (1599936575607926785, 1599936575524040705, 'search', 225, '2022-12-06 09:19:40');
INSERT INTO `j_relation_module_template` VALUES (1599941310569979906, 1599941309659815938, 'table', 226, '2022-12-06 09:38:29');
INSERT INTO `j_relation_module_template` VALUES (1599941353569984513, 1599941353247023106, 'search', 226, '2022-12-06 09:38:39');
INSERT INTO `j_relation_module_template` VALUES (1599946720110649346, 1599946719326314498, 'table', 227, '2022-12-06 09:59:58');
INSERT INTO `j_relation_module_template` VALUES (1599946770538766337, 1599946769884454914, 'search', 227, '2022-12-06 10:00:10');
INSERT INTO `j_relation_module_template` VALUES (1599948780080144386, 1599948779560050690, 'search', 227, '2022-12-06 10:08:09');
INSERT INTO `j_relation_module_template` VALUES (1600024900213555202, 1600024900075143170, 'search', 232, '2022-12-06 15:10:38');
INSERT INTO `j_relation_module_template` VALUES (1600025648552886274, 1600025648267673601, 'search', 232, '2022-12-06 15:13:36');
INSERT INTO `j_relation_module_template` VALUES (1600304877324726274, 1600304877169537025, 'table', 232, '2022-12-07 09:43:10');
INSERT INTO `j_relation_module_template` VALUES (1600739150066298882, 1600739149965635585, 'search', 232, '2022-12-08 14:28:48');
INSERT INTO `j_relation_module_template` VALUES (1600739207012364290, 1600739206408384513, 'table', 232, '2022-12-08 14:29:02');
INSERT INTO `j_relation_module_template` VALUES (1601027648723824642, 1601027648505720834, 'search', 245, '2022-12-09 09:35:12');
INSERT INTO `j_relation_module_template` VALUES (1601028322060611586, 1601028321938976769, 'search', 245, '2022-12-09 09:37:52');
INSERT INTO `j_relation_module_template` VALUES (1601028469125492738, 1601028468878028801, 'table', 245, '2022-12-09 09:38:27');

-- ----------------------------
-- Table structure for j_tab_item
-- ----------------------------
DROP TABLE IF EXISTS `j_tab_item`;
CREATE TABLE `j_tab_item`  (
  `item_id` bigint(20) NOT NULL,
  `item_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `item_order` int(11) NULL DEFAULT NULL,
  `tab_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_tab_item
-- ----------------------------
INSERT INTO `j_tab_item` VALUES (1001, 'itemA', 1, 1);
INSERT INTO `j_tab_item` VALUES (1002, 'itemB', 2, 1);
INSERT INTO `j_tab_item` VALUES (1003, 'itemC', 3, 1);
INSERT INTO `j_tab_item` VALUES (1004, 'itemT', 1, 5);

-- ----------------------------
-- Table structure for j_template_resource
-- ----------------------------
DROP TABLE IF EXISTS `j_template_resource`;
CREATE TABLE `j_template_resource`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `DOCCHANNELID` bigint(20) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0 COMMENT '删除状态',
  `SINGLETEMPKATE` bigint(20) NULL DEFAULT NULL,
  `SITEID` bigint(20) NULL DEFAULT NULL COMMENT '站点id',
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` smallint(6) NULL DEFAULT 0 COMMENT '状态',
  `COMPANYID` bigint(20) NULL DEFAULT NULL COMMENT '所属机构id',
  `WEBSITEID` bigint(20) NULL DEFAULT NULL COMMENT '站点id',
  `COLUMNID` bigint(20) NULL DEFAULT NULL COMMENT '栏目id',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `SEQENCING` int(10) NULL DEFAULT NULL COMMENT '排序',
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作流id',
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作流用户',
  `QUOTAINFO` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `COPYFROMID` bigint(20) NULL DEFAULT NULL,
  `TITLE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `RDESC` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `CONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'neirong',
  `PATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `SITENAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站点名称',
  `TYPE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型(js,css,img)',
  `TEMPLATE_ID` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `TEMPLATE_NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `EXT` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后缀',
  `FULL_PATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '完整发布路径',
  `PUB_URL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布地址',
  `VISUAL_TYPE` int(2) NULL DEFAULT NULL COMMENT '可视化类型(1:组件,2:产品)',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 488 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模板资源(js,css,img)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_template_resource
-- ----------------------------

-- ----------------------------
-- Table structure for j_visual_module_type
-- ----------------------------
DROP TABLE IF EXISTS `j_visual_module_type`;
CREATE TABLE `j_visual_module_type`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `TITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `TYPE_CODE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `PARENT_ID` bigint(20) NULL DEFAULT NULL COMMENT '父级ID',
  `PARENT_TITLE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级名称',
  `ICON` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 107 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组件类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_visual_module_type
-- ----------------------------
INSERT INTO `j_visual_module_type` VALUES (6, 'admin', '2022-08-30 11:36:26', 1167324599977414658, 0, NULL, NULL, '2022-10-28 09:02:53', '自定义组件', NULL, 0, NULL, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3FpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDozNjhkNjE4YS1jODMyLTk0NGQtYWI5Zi1hMTFhNWI1MDM0N2YiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MzdCQ0FFQ0NCNDY2MTFFQkE1MzBGOEM0MEQ0MUU1MzIiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MzdCQ0FFQ0JCNDY2MTFFQkE1MzBGOEM0MEQ0MUU1MzIiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjM2OGQ2MThhLWM4MzItOTQ0ZC1hYjlmLWExMWE1YjUwMzQ3ZiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDozNjhkNjE4YS1jODMyLTk0NGQtYWI5Zi1hMTFhNWI1MDM0N2YiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz6kVNIvAAABAUlEQVR42mJ0C45lwAEcgLgfiAWAeCIQT8CmaOeaRSh8JhyGgQxZD8QGQKwANdiAgQiAy0ADqKHIIIAYA1mQXCSA5kJ0wA91LQx8gGIMFyYA8X00vB6LgQVoat5DxTAMjMfhImJAv3tInAK6gQYMlAEBYiKFbEB1AxmBCTsByraHRhCpAJTgHwLxAWAiv8CIllMKoImYXJCI7uUFFPq4n9ph+IAJS+IlF4ByzUQWqCHy0NKFnDRZCMQXQAaCIgVkYD0FOYUBFrvEpENQBBkCsSDUFQyklDYYgQs15ANSWtMnJp0yYSuCgGADFvGNOCLiAbqBieiCJMRqIzD8UCwGCDAAKg08AwUYcboAAAAASUVORK5CYII=');
INSERT INTO `j_visual_module_type` VALUES (68, 'string', '2022-08-15 11:36:26', 0, 0, NULL, 'admin', NULL, '文献情报列表页', NULL, 6, '自定义组件', NULL);
INSERT INTO `j_visual_module_type` VALUES (69, 'string', '2022-08-15 11:38:08', 0, 0, NULL, 'admin', NULL, '文献情报内容页', NULL, 6, '自定义组件', NULL);
INSERT INTO `j_visual_module_type` VALUES (73, 'admin', '2022-08-29 16:42:30', 1167324599977414658, 0, 1167324599977414658, 'admin', '2022-08-29 17:28:59', '基础组件', NULL, 0, NULL, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAMAAADTXB33AAAAw1BMVEUAAAAAAABVVVUzZmZVVVVVVVVGXV1AVVVOTmJAUGBHVWNDUV5NWVlGUV1HUlxFWFhEU1pGU2BEV11IU15GVV9IU11GVF5IVF1GVFxHVV1GVV1IU11HVF1IVV5HVF1IVF1IVF5GVF1HU15HVF1HVF5GU11HVF1HU11HU11HU11GVF1HU11HVF5HVF1IVFxHVF5GVV1HVFxHVV1HVF1GVF1HVF1HVF1HVFxHVV1HVF1HVF1HVF1HVF1HVF1HVF1HVF3///8d8wFVAAAAP3RSTlMAAQMFBgkLDA0QEhMUFhkaIigpLjNHSVVbXWZuc3V2fICDkJebnJ2foqWnqKmqq7S1tru/wMXGz9DU2Ozv8fSNIYWMAAAAAWJLR0RA/tlc2AAAALZJREFUKM/NkNcWgjAQRBckiIoNe+8NO/a6//9XEpeAQY++Ok9zz+RsZhcgLLVZFTadIemE2gKxqzxtDT1d4hzZivuByn1fRJhzSXfITyPhKLYXsGRyZB59QIdJUQNfVJKi1DlIdlH5r6Q/cWuEG4oevEUw/2byXYyN3x3AypMSdA22Rhyq8FFap67AL2VHc1KLvy3bBJOCCz2/oeXSVcDh7bx3Aaf/ib6Ut8beym2+cmVGYBcBHhKjWrnYlbhrAAAAAElFTkSuQmCC');
INSERT INTO `j_visual_module_type` VALUES (74, 'admin', '2022-08-29 16:44:16', 1167324599977414658, 0, NULL, NULL, NULL, '应用组件', NULL, 0, NULL, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAAUCAMAAABYi/ZGAAAA+VBMVEUAAAAAAACAgIBAQEAzZmZVVVVJSW1AYGA5VVVAVVVAUGBAWVlGUV1DTllHUlxFTlhJUltKUlpJUF9FU1pDUV5EUV1JVVtEVVtIU15EUltIVV5GVFxHVFxFU11GU11HVF5GU1xFVF1HVFxHVFxFU11FU15HUl1GVFxHU11GVF5GVF1FU15GU15GU11GU11FUl5GU1xFU11GUl1FU15GU11GU11FVF5GU11GU1xGVF1HU15GVF1GUl1GU11GU15FU11GU11GU15GU11GU11GU11GU11GU11GU11GU11GU11GU11GU11GU11GU11GU11GU11GU11GU13////A+9L7AAAAUXRSTlMAAQIEBQYHCAkMEBQWFxkaHB8jJSYpKi0uODk6PUpNT1BVXmFrcnN0foCGiIuRnZ6goqepsrO0tba3vL3A0dLV2Nrc3d/j5Ozv8/T19/j5+v6qvbu8AAAAAWJLR0RSDWAtkAAAALxJREFUGNN90NUSglAQgOG1sTsRu7ETExULBXXf/2WkRMQZ/6tzvovdmQVQclTYS8MFhuylPUodS7pYizvUWiRUim7x0512y+ZfobFjCCBFWMrnD11pTxJ4jgJv/6lJ20dxNxAQJ2HIspLcOoHgQJopG/J1u7M2bvltVR7fhrjJSNvSa3W3ZiiQQGpP3XAEQzQbA9O/tv8xDvKCycQCQGT2Zcu4cpncQbeTfkKiKSr26LgNl44xPejOY+rnBViVTsppMFnAAAAAAElFTkSuQmCC');
INSERT INTO `j_visual_module_type` VALUES (75, 'admin', '2022-08-29 17:08:06', 1167324599977414658, 0, 1167324599977414658, 'admin', '2022-08-29 17:30:04', '布局', NULL, 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo2ODAyNDYwMUIyRjAxMUVCQjFDQkU0QUE0RTEwRDBFNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo2ODAyNDYwMkIyRjAxMUVCQjFDQkU0QUE0RTEwRDBFNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjY4MDI0NUZGQjJGMDExRUJCMUNCRTRBQTRFMTBEMEU0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjY4MDI0NjAwQjJGMDExRUJCMUNCRTRBQTRFMTBEMEU0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+y0EurwAAAFpJREFUeNpidAuO/c9AB8DEQCfAAmPsXLOIkRYWuIfE/aerj+gfdOggsvQmWYlkebc64+D0ES6XwXyKS37A42jUolGLRi0atYiEso7UMnAEBx2sbh/yPgIIMACK0BLbw9bfUAAAAABJRU5ErkJggg==');
INSERT INTO `j_visual_module_type` VALUES (76, 'admin', '2022-08-29 17:08:59', 1167324599977414658, 0, NULL, NULL, NULL, '图片', NULL, 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo4RkYzMTZCMEIyRjAxMUVCQjFEOEMyNUZEM0VFNjVGRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo4RkYzMTZCMUIyRjAxMUVCQjFEOEMyNUZEM0VFNjVGRSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjhGRjMxNkFFQjJGMDExRUJCMUQ4QzI1RkQzRUU2NUZFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjhGRjMxNkFGQjJGMDExRUJCMUQ4QzI1RkQzRUU2NUZFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+J1/0tAAAAdNJREFUeNrsVT1LA0EQndndu+QfpIlYiYXiHxDbmEbBDwTB2NiIWAiWCjaCSKKtlY0ggthYiY2lItj4DwQFLcTGQoy3N76500IUuRgNKO4yt5PZ23nz8TbHpZGKUAuGoRYN96oc7m3xTwD0j05KSzP6e0DuI+PQ3DkJC1myfTmXWxGSExFZxNaj7nvxFMURYY+EYshb4u5Wu7JlFFhHIQfkrB2Gk174qTBxOySZT/7pe0pXh6NHX0fU/oiZr2OJj5HFrWai8m2lS4aSXeiAhUd87C/rUr9jSm+AlgplVTXUuJomAxxGyOAEHbhKsdPp2KlMwFSTZjNSh5FExJ7JGsTjA7Q9bXzowgL2l7VvIMMpbNtE3ERGIirOsuEc5xLwF9sinu2pA7MGKfJXgDRqkIDyNg/muU2UZ0FJYNmCkWHZsJlOQBNac4HFrSlZv5SROgpsMAigSYAuwdSDzPKhddV3JWca07+1hnsk6WyD1NKg2RnHqyLxBX52f3QCgW3gxTOoN5mBUDIUjmehdrz40ajLzOaT22CKeHEd6hTkIVPp0Isy+j/b6KVEcONYSo3QewCHlEb3DWJZnJvHup8JaKfWOYNl5v97lIner9/2X5/RswADAPl7oEIbWeWPAAAAAElFTkSuQmCC');
INSERT INTO `j_visual_module_type` VALUES (77, 'admin', '2022-08-29 17:10:31', 1167324599977414658, 0, NULL, NULL, NULL, '按钮', NULL, 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBNTJBQTQ3Q0IyRjAxMUVCQUU0Q0U0QzE3OTQ0Q0RBOSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBNTJBQTQ3REIyRjAxMUVCQUU0Q0U0QzE3OTQ0Q0RBOSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkE1MkFBNDdBQjJGMDExRUJBRTRDRTRDMTc5NDRDREE5IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkE1MkFBNDdCQjJGMDExRUJBRTRDRTRDMTc5NDRDREE5Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Aam6nAAAAbZJREFUeNrsVT1IQlEUPueqgyAERVMtgWRjNFVbRTVFw2vQooZAyDEoWosoiFqa2hyEqMimIIjAoCHaIlwkiLZCbTMizXtP37OksMLXkBB44XB+3rvn+865577HQ9akUA2Wohotd9k4jsf4LwCGx6akphXVgf7R1H1ewfnUPgv3wHyCbAvJKXSEiQehcxAPwREt41Ah0PVrLoZgZ4k4rERFdjYCXVWBABIQlgQAMorUIgk1i5goKeVFslH4l0ZkT1w6jectilwDiI9gaxRiQKLVeeuEkgDcEpE7OBA6gb9sJwKJI1y6Ndgp0GrCO48sagEQbYg9/+aMXGAYgU6AXQbJYsyqTOBNsYFZchohq8yMNvMEbB9EO64ISdIAu4J0gunc160fOQxrMWJWWPEMfD+k4BTITntoRFto0wHY9r/HX749U2ZfXufjQEtiEKYRanAKhEKoHSoIoxvVZatML3Pp8PQS9tk5vY6AUMU1lIVWbJLIuRgza8eNMjk8O0MF96wxawW3PWMPIFL0uD2kWF3AjqPvN47u0e56h/UD81tIX0Wst8IP1791dSBnX+/yv/3fV/QqwACIPp29+I3EEwAAAABJRU5ErkJggg==');
INSERT INTO `j_visual_module_type` VALUES (78, 'admin', '2022-08-29 17:10:44', 1167324599977414658, 0, NULL, NULL, '2022-12-06 07:04:22', '表格', 'table', 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFQ0Y1NkU5Q0IyRjAxMUVCQjU5QUU3OTQ4MDFDMjEzQiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFQ0Y1NkU5REIyRjAxMUVCQjU5QUU3OTQ4MDFDMjEzQiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkVDRjU2RTlBQjJGMDExRUJCNTlBRTc5NDgwMUMyMTNCIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkVDRjU2RTlCQjJGMDExRUJCNTlBRTc5NDgwMUMyMTNCIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+McTgCAAAAGFJREFUeNpidAuO/c9AB8DEQCfAAmPsXLOIkRYWuIfE/aerj4afRfA4iiy9+X94+Wh5tzrWVAfzKbny7iEMwzTVMcKKICHFappY8O5+62hiGC2CRlPdaKojlBiGjY8AAgwARt81jcm9HB4AAAAASUVORK5CYII=');
INSERT INTO `j_visual_module_type` VALUES (79, NULL, '2022-09-14 11:04:47', NULL, 0, NULL, NULL, NULL, '测试组件', NULL, 0, NULL, 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFQ0Y1NkU5Q0IyRjAxMUVCQjU5QUU3OTQ4MDFDMjEzQiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFQ0Y1NkU5REIyRjAxMUVCQjU5QUU3OTQ4MDFDMjEzQiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkVDRjU2RTlBQjJGMDExRUJCNTlBRTc5NDgwMUMyMTNCIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkVDRjU2RTlCQjJGMDExRUJCNTlBRTc5NDgwMUMyMTNCIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+McTgCAAAAGFJREFUeNpidAuO/c9AB8DEQCfAAmPsXLOIkRYWuIfE/aerj4afRfA4iiy9+X94+Wh5tzrWVAfzKbny7iEMwzTVMcKKICHFappY8O5+62hiGC2CRlPdaKojlBiGjY8AAgwARt81jcm9HB4AAAAASUVORK5CYII=');
INSERT INTO `j_visual_module_type` VALUES (80, NULL, '2022-10-20 11:06:39', NULL, 0, NULL, NULL, NULL, '导航', NULL, 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAIAAABr4HqSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoyMERBQ0Y5MDUwMjQxMUVEOTdFRUMwOEVFODNCODc1NyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDoyMERBQ0Y5MTUwMjQxMUVEOTdFRUMwOEVFODNCODc1NyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjIwREFDRjhFNTAyNDExRUQ5N0VFQzA4RUU4M0I4NzU3IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjIwREFDRjhGNTAyNDExRUQ5N0VFQzA4RUU4M0I4NzU3Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+PhQxhwAAASpJREFUeNpidAuOZaAeYGKgKmCBUDvXLKLQIPeQOOq7jijj1ux6c/DMRxI8ix9cu/tdi6SwQ9L57fq972iCb97/vn6PYe3ut3AReUl2Ex0eooxD1hbnJwb05uv3v4GM6Suff/vxDy6VES5hb8JPWtit2f0GaJC8FDuQUZwgzcWBUD9j5YuHz36SZty37/96Fz4FmigqyLpo0ys0E5tmPEIzkYiY/Q8igP6FchlRLHv97jcJxgHdAnQR0F2QcAS6FGgEcvChRQgB47zshIBmAX3kZSuIZpanrSBmVKDHrKgQq6YSF5x79c43Lg5moBtX73orL8kBF1eQZgc6lnBCAVqIaWfT9MdaypwhbiL0LlFGjaOxccBoBSYgqpV3xCQRFOMgJf2gCzuAAAMAPlFxpwJnypAAAAAASUVORK5CYII=');
INSERT INTO `j_visual_module_type` VALUES (81, NULL, '2022-10-20 11:07:43', NULL, 0, NULL, NULL, NULL, '菜单', NULL, 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAIAAABr4HqSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDozQTFCMUNGMzUwMjQxMUVEQjc0Njk5MzVDODhENkVDQSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDozQTFCMUNGNDUwMjQxMUVEQjc0Njk5MzVDODhENkVDQSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjNBMUIxQ0YxNTAyNDExRURCNzQ2OTkzNUM4OEQ2RUNBIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjNBMUIxQ0YyNTAyNDExRURCNzQ2OTkzNUM4OEQ2RUNBIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+LS19QgAAAWdJREFUeNpidAuOZaAeYGKgKmCBUDvXLKLQIPeQOIRxEPDw2c81u998+/4PWVBUkDXOTwzIWLTp1ev3v5GluDiZQlxF5KXY0V0HAWt2vdFS5kKWBoJrd78t3PQKbC6LnQkfuvW73hQnSGM37uv3f0CzgCaieWTNrrdA0t6ED1Pq9JUvNIyKEWUcSlRwczIBI4uREUXF9XvfRYVYIQw0KaBioBacxoW4iQAjHi2ygBoywyWBjOkrn1+98w1NCqgFp2eByRLoEKATkBHEaUDAycH06/c/ZAQUERFgxem6RRtfAdNdsKswWjIGZpUXb35fv/eNlRnFt09f/Xr19ndDthzOZAxMqFiT8fef/4BmsbAwokn9+Pl/NBnTxji0Eg2SVoFphZOd8e///2hSQBEOdkacCSXeX2zhxlcHT3/CTKtfv/1bvv0VWjxyczIHuYrgNA6Y6OoyZXF5pDlXntg8CynpB11UAAQYAPDfkDCPMo2NAAAAAElFTkSuQmCC');
INSERT INTO `j_visual_module_type` VALUES (90, NULL, '2022-11-01 11:49:20', NULL, 0, NULL, NULL, NULL, '1', NULL, 79, '测试组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo2ODAyNDYwMUIyRjAxMUVCQjFDQkU0QUE0RTEwRDBFNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo2ODAyNDYwMkIyRjAxMUVCQjFDQkU0QUE0RTEwRDBFNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjY4MDI0NUZGQjJGMDExRUJCMUNCRTRBQTRFMTBEMEU0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjY4MDI0NjAwQjJGMDExRUJCMUNCRTRBQTRFMTBEMEU0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+y0EurwAAAFpJREFUeNpidAuO/c9AB8DEQCfAAmPsXLOIkRYWuIfE/aerj+gfdOggsvQmWYlkebc64+D0ES6XwXyKS37A42jUolGLRi0atYiEso7UMnAEBx2sbh/yPgIIMACK0BLbw9bfUAAAAABJRU5ErkJggg==');
INSERT INTO `j_visual_module_type` VALUES (91, NULL, '2022-11-01 11:52:47', NULL, 0, NULL, NULL, NULL, '文本', NULL, 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo2ODAyNDYwMUIyRjAxMUVCQjFDQkU0QUE0RTEwRDBFNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo2ODAyNDYwMkIyRjAxMUVCQjFDQkU0QUE0RTEwRDBFNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjY4MDI0NUZGQjJGMDExRUJCMUNCRTRBQTRFMTBEMEU0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjY4MDI0NjAwQjJGMDExRUJCMUNCRTRBQTRFMTBEMEU0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+y0EurwAAAFpJREFUeNpidAuO/c9AB8DEQCfAAmPsXLOIkRYWuIfE/aerj+gfdOggsvQmWYlkebc64+D0ES6XwXyKS37A42jUolGLRi0atYiEso7UMnAEBx2sbh/yPgIIMACK0BLbw9bfUAAAAABJRU5ErkJggg==');
INSERT INTO `j_visual_module_type` VALUES (104, NULL, '2022-11-02 10:34:03', NULL, 0, NULL, NULL, NULL, '轮播', NULL, 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpGODc2MjFCRTVBNTUxMUVEQTM4RTk4QkU4ODk3Q0I4QSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpGODc2MjFCRjVBNTUxMUVEQTM4RTk4QkU4ODk3Q0I4QSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkY4NzYyMUJDNUE1NTExRURBMzhFOThCRTg4OTdDQjhBIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkY4NzYyMUJENUE1NTExRURBMzhFOThCRTg4OTdDQjhBIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+H6XBkgAAAg9JREFUeNrsls9rE0EUx7+zM5sm2U3a2EpVxKKYQg/1D+jBo5QKQj0ECi2Kgv+Cf4VHL160oBR6KPRSSqE99CCFVipSUEypNaEQafNrN8km6W7WmUly2GYPahOh0HeYHd7Oe595M/PeDJlKPE0ZpfK1bC5vUUrRTXEcB4NXYqGormWYUSpF4nduq3OJabViWV0FhUMhbO3sInnwI8KKRRNjo3fx4skMeiFFw8TO7hco+E/CRNPemr1kGVbVhXIOvOtyp4xg5EYAsajqBQ0NqNjYBl5/SCM+EgQRBv8IItw4naljPB7Gy+c3vSARQbZg4+H9GJ49Hj73Mq1vFbD5yfDolHa4CiUy5G4IFb4o6QTJkOW5d7uUP66cvC+o13IJugT9Hah+2kDZcjr0Qif+/XGtQ6vkUEp8IC4WV7PS6czUVax9LEj9g4kBLKwcQwtRJCaHEFCJJ2EJ8QEJpcszzC9hKxxw9Ksmx+QMG2neFyL6RdNGgX/FmIDKPAlrn/HF2iUoEmZ4v5xD8qclw3NbE6AKwX6qikq1uXzHeVsavl3K4PO3CsJBBa/eHcFpNKuBsEm1imoH6CR/ijl+710fvIVanV8TZ8LWNcoLL4HBIwj2Nbe1Wmtg9tEwGhxQKju+10QHyG6NuzeqXfzjzfr7I/j6fR9v5hfQi8eJ8C0YLKrrZvLgUOOvlR4+t3TztwADAKB316Qq1iSjAAAAAElFTkSuQmCC');
INSERT INTO `j_visual_module_type` VALUES (105, NULL, '2022-11-18 13:58:10', NULL, 0, NULL, NULL, NULL, '表单', NULL, 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpEMjlCRTY1ODY2RjUxMUVEQUI5RUFCMzBCQ0FEQzU4OCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpEMjlCRTY1OTY2RjUxMUVEQUI5RUFCMzBCQ0FEQzU4OCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkQyOUJFNjU2NjZGNTExRURBQjlFQUIzMEJDQURDNTg4IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkQyOUJFNjU3NjZGNTExRURBQjlFQUIzMEJDQURDNTg4Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+8W8dZwAAAj1JREFUeNpi9ApLePLx8xfej58+M9AC8PPxMvDz8nxm+fTli6iWmgqbloYqTSy6duM2w/XbdzhY3rx7/9PKzJgtISqEJhYtWLaG4fDJ0z+ZWJiZGb5++8ZAKwAyG2QHC7Lg2w+/GX79/s8gKcrG8O7jHzBmYgRKMBJv8L9//xkEeFkYRARZUcQhFkENapj2iOHl298McxpVGaYsfc6wfu9bYEQyMzCSYNHvP/8Z5CTYGWY1qjDwcDGjWfQfwjHT5WV4+/4PmK2pzMnw45cAAzeSYmLA33/AlMbNzMDMzIjFR1AQ6yvG8B9oKcgHoe4iDP5OQqSFG8jNQAPY2ZgwxFnQBZCDiY2ViYzox+4wJgY6AQwf/QFGJgsLIzj1ffvxF48bcaQ6YNBxc7IAQ4MRd6prnvkYmMT/MLTkyjHMW/+S4cCpj8BUx0KSRX+ByZsPqKc9Xx5LqiMUwaQkBhwaUJJ3bbosPOgywiQZ4kCpkJHEoAP6iIcLV9AhC7AwQlMcIxCzUC0xDFyqA/ocUr6BihNgyiM16EAZHj3YMCyav+4lw8cvfxmSgsQZdh19z3D+xlcGLk5m0lId0CY+YGpLC5Vg4ORgwp68z9/8wvDi9W+wRVfvfmfYdugdJHmTUagmB4vjTnX1mXLgjMrHw8yQGy3JEO0jSnY1wYNWGKMEnbAAog4R4mcBY6qluj9//wKrAi6apTaQ2SA7WESEBNmPnTrL8OHTJ5o1TkB2sPDx8Ly+dusO7/Ez52na3AIIMAAYrcBSXLp31gAAAABJRU5ErkJggg==');
INSERT INTO `j_visual_module_type` VALUES (106, NULL, '2022-12-01 17:03:21', NULL, 0, NULL, NULL, '2022-12-06 08:42:37', '搜索', 'search', 73, '基础组件', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAYCAYAAADkgu3FAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpEMjlCRTY1ODY2RjUxMUVEQUI5RUFCMzBCQ0FEQzU4OCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpEMjlCRTY1OTY2RjUxMUVEQUI5RUFCMzBCQ0FEQzU4OCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkQyOUJFNjU2NjZGNTExRURBQjlFQUIzMEJDQURDNTg4IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkQyOUJFNjU3NjZGNTExRURBQjlFQUIzMEJDQURDNTg4Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+8W8dZwAAAj1JREFUeNpi9ApLePLx8xfej58+M9AC8PPxMvDz8nxm+fTli6iWmgqbloYqTSy6duM2w/XbdzhY3rx7/9PKzJgtISqEJhYtWLaG4fDJ0z+ZWJiZGb5++8ZAKwAyG2QHC7Lg2w+/GX79/s8gKcrG8O7jHzBmYgRKMBJv8L9//xkEeFkYRARZUcQhFkENapj2iOHl298McxpVGaYsfc6wfu9bYEQyMzCSYNHvP/8Z5CTYGWY1qjDwcDGjWfQfwjHT5WV4+/4PmK2pzMnw45cAAzeSYmLA33/AlMbNzMDMzIjFR1AQ6yvG8B9oKcgHoe4iDP5OQqSFG8jNQAPY2ZgwxFnQBZCDiY2ViYzox+4wJgY6AQwf/QFGJgsLIzj1ffvxF48bcaQ6YNBxc7IAQ4MRd6prnvkYmMT/MLTkyjHMW/+S4cCpj8BUx0KSRX+ByZsPqKc9Xx5LqiMUwaQkBhwaUJJ3bbosPOgywiQZ4kCpkJHEoAP6iIcLV9AhC7AwQlMcIxCzUC0xDFyqA/ocUr6BihNgyiM16EAZHj3YMCyav+4lw8cvfxmSgsQZdh19z3D+xlcGLk5m0lId0CY+YGpLC5Vg4ORgwp68z9/8wvDi9W+wRVfvfmfYdugdJHmTUagmB4vjTnX1mXLgjMrHw8yQGy3JEO0jSnY1wYNWGKMEnbAAog4R4mcBY6qluj9//wKrAi6apTaQ2SA7WESEBNmPnTrL8OHTJ5o1TkB2sPDx8Ly+dusO7/Ez52na3AIIMAAYrcBSXLp31gAAAABJRU5ErkJggg==');

-- ----------------------------
-- Table structure for j_visual_template_type
-- ----------------------------
DROP TABLE IF EXISTS `j_visual_template_type`;
CREATE TABLE `j_visual_template_type`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `TITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组件类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of j_visual_template_type
-- ----------------------------
INSERT INTO `j_visual_template_type` VALUES (39, 'admin', '2022-08-08 16:44:26', 1167324599977414658, 0, NULL, NULL, NULL, 'E-安保');
INSERT INTO `j_visual_template_type` VALUES (40, 'admin', '2022-08-08 16:44:26', 1167324599977414658, 0, NULL, NULL, NULL, '项目1');
INSERT INTO `j_visual_template_type` VALUES (41, 'admin', '2022-08-08 16:44:26', 1167324599977414658, 0, NULL, NULL, NULL, '项目2');
INSERT INTO `j_visual_template_type` VALUES (42, 'admin', '2022-08-08 16:44:26', 1167324599977414658, 0, NULL, NULL, NULL, '项目3');
INSERT INTO `j_visual_template_type` VALUES (49, 'string', '2022-08-19 15:59:14', 0, 0, NULL, NULL, '2022-10-27 18:01:56', '机床');

-- ----------------------------
-- Table structure for jmeta_yltable
-- ----------------------------
DROP TABLE IF EXISTS `jmeta_yltable`;
CREATE TABLE `jmeta_yltable`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL,
  `DOCCHANNELID` bigint(20) NULL DEFAULT NULL,
  `DOCSTATUS` smallint(2) NULL DEFAULT 0,
  `SINGLETEMPKATE` bigint(20) NULL DEFAULT NULL,
  `SITEID` bigint(20) NULL DEFAULT NULL,
  `DOCVALID` date NULL DEFAULT NULL,
  `DOCPUBTIME` datetime NULL DEFAULT NULL,
  `OPERUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OPERTIME` datetime NULL DEFAULT NULL,
  `DOCTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOCRELTIME` datetime NULL DEFAULT NULL,
  `DOCPUBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LINKURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CLASSINFOID` bigint(10) NULL DEFAULT NULL,
  `STATUS` smallint(6) NULL DEFAULT 0,
  `COMPANYID` bigint(20) NULL DEFAULT NULL,
  `WEBSITEID` bigint(20) NULL DEFAULT NULL,
  `COLUMNID` bigint(20) NULL DEFAULT NULL,
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SEQENCING` int(10) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `FLOW_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `FLOW_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `QUOTAINFO` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `COPYFROMID` bigint(20) NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `COLOR` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品颜色',
  `SIZE` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品大小',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品表(样例元数据表)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jmeta_yltable
-- ----------------------------

-- ----------------------------
-- Table structure for job_api
-- ----------------------------
DROP TABLE IF EXISTS `job_api`;
CREATE TABLE `job_api`  (
  `id` bigint(20) NOT NULL,
  `type` int(11) NULL DEFAULT NULL COMMENT '类别：1新闻发布、2新闻撤稿、',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时API任务的描述',
  `siteid` bigint(20) NULL DEFAULT NULL COMMENT '网站id',
  `columnid` bigint(20) NULL DEFAULT NULL COMMENT '栏目id',
  `docid` bigint(20) NULL DEFAULT NULL COMMENT '融媒体id',
  `doctitle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '融媒体标题',
  `jobtime` datetime NULL DEFAULT NULL COMMENT '定时任务执行时间',
  `requrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时API任务的请求链接（已拼接了query等）',
  `reqmethod` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时API任务的请求Method（get/put/post等）',
  `reqbody` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时API任务的请求参数体',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态：0未执行、1请求成功、2请求失败、3执行异常',
  `result` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '定时任务执行结果',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称(系统会自动维护）',
  `MODIFY_USER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称(系统会自动维护）',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时API任务详情（每条记录对应一个定时的API请求）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job_api
-- ----------------------------

-- ----------------------------
-- Table structure for limit_info
-- ----------------------------
DROP TABLE IF EXISTS `limit_info`;
CREATE TABLE `limit_info`  (
  `ID` bigint(20) NOT NULL COMMENT '主键id',
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `TYPE` bigint(20) NULL DEFAULT NULL COMMENT '权限类型',
  `VALUE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限值',
  `DESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `SYSTEM` int(1) NULL DEFAULT 0 COMMENT '是否系统预定义权限',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `STATUS` int(20) NULL DEFAULT NULL COMMENT '转换修改状态',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `LDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限类型',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of limit_info
-- ----------------------------
INSERT INTO `limit_info` VALUES (1263303220428554242, '新建机构', 1156097992882716674, '1', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 21:58:53', 0, 1167324599977414658, '2020-05-28 03:35:09', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303317291810817, '修改机构', 1156097992882716674, '1', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 21:59:17', 0, 1167324599977414658, '2020-05-27 16:19:59', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303385503776770, '删除机构', 1156097992882716674, '1', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 21:59:33', 0, 1167324599977414658, '2020-05-27 16:19:59', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303434598105090, '查看机构列表', 1156097992882716674, '1', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 21:59:45', 0, 1167324599977414658, '2020-05-28 03:39:22', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303479129030658, '机构排序', 1156097992882716674, '1', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 21:59:55', 0, 1167324599977414658, '2020-05-27 16:19:59', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303538209996802, '机构权限设置', 1156097992882716674, '1', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:00:09', 0, 1167324599977414658, '2020-05-27 16:19:59', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303596384993282, '新建站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:00:23', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303645244440577, '修改站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:00:35', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303706858766337, '删除站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:00:49', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303773946658817, '导入站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:01:05', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303823397502978, '导出站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:01:17', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303869266411522, '停用站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:01:28', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303920004907009, '恢复站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:01:40', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263303986782420993, '复制站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:01:56', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304034257747969, '发布站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:02:07', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304130747711490, '发布首页', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:02:30', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304204739428353, '移动站点', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:02:48', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304254840389634, '查看站点列表', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:03:00', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304300864487425, '站点回收站', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:03:11', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304361577037825, '站点权限设置', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:03:26', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304511083003906, '栏目回收站', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:04:01', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304596516782081, '栏目还原', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:04:22', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304716473876481, '栏目回收站清空', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:04:50', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304759851368449, '站点分发', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:05:00', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304804759781377, '站点排序', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:05:11', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263304846006566914, '工作流管理', 1196265228058923010, '2', NULL, 1, 'admin', 1167324599977414658, '2020-05-20 22:05:21', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263756773978824706, '新建栏目', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:01:09', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263756811312324610, '修改栏目', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:01:18', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263756856220737537, '删除栏目', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:01:29', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263756918883639298, '导入栏目', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:01:44', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263756967264935938, '导出栏目', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:01:55', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757016589950977, '复制栏目', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:02:07', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757092234223617, '发布栏目', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:02:25', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757146286219265, '移动栏目', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:02:38', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757194189365249, '查看栏目列表', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:02:49', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757242105094146, '栏目权限管理', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:03:01', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757288645091329, '子栏目管理', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:03:12', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757328591642626, '栏目高级属性', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:03:21', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757372099158017, '栏目同步', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:03:32', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757416491671554, '元数据回收站', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:03:42', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757455406424066, '元数据信息列表', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:03:52', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757503682863106, '元数据查看', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:04:03', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757549136535554, '发布栏目前端页面', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:04:14', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757595240325122, '栏目排序', 1196265982328999937, '3', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:04:25', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757771468201986, '新建元数据信息', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:05:07', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757853206798338, '修改元数据信息', 0, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:05:26', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263757989932720129, '删除元数据信息', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:05:59', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263758143721070594, '导入元数据信息', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:06:36', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263758195663331330, '导出元数据信息', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:06:48', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263764457545293825, '元数据信息审核', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:31:41', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263764525123919873, '元数据信息引用', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:31:57', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263764576772579329, '元数据信息复制', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:32:09', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263764628626759682, '元数据信息移动', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:32:22', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263764726630866946, '元数据信息排序', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:32:45', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263764800555474945, '元数据信息置顶', 1189100639360135170, '4', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:33:03', 0, 1167324599977414658, '2020-05-27 16:20:00', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263764975034327042, '前台菜单新建', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:33:44', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765081695477762, '前台菜单修改', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:34:10', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765157973090305, '前台菜单删除', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:34:28', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765206920617985, '前台菜单列表查看', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:34:40', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765263455641602, '前台菜单排序', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:34:53', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765305318989825, '前台菜单移动', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:35:03', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765353423462401, '后台菜单新建', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:35:15', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765408494673921, '后台菜单修改', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:35:28', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765451851194370, '后台菜单删除', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:35:38', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765495258046465, '后台菜单列表查看', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:35:48', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765570784878594, '后台菜单排序', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:36:06', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765632290152449, '后台菜单移动', 1161946170935865345, '5', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:36:21', 0, 1167324599977414658, '2020-05-27 16:20:01', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765708274163714, '设置子组织', 1265885567753691137, '6', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:36:39', 0, 1167324599977414658, '2020-05-29 00:33:15', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765749466423297, '查看同级组织', 1265885567753691137, '6', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:36:49', 0, 1167324599977414658, '2020-05-29 00:33:15', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765805233889282, '子组织继承上一级组织权限', 1265885567753691137, '6', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:37:02', 0, 1167324599977414658, '2020-05-29 00:33:15', NULL, 'admin');
INSERT INTO `limit_info` VALUES (1263765897542131714, '设置组长', 1265885567753691137, '6', NULL, 1, 'admin', 1167324599977414658, '2020-05-22 04:37:24', 0, 1167324599977414658, '2020-05-29 00:33:15', NULL, 'admin');

-- ----------------------------
-- Table structure for limit_type
-- ----------------------------
DROP TABLE IF EXISTS `limit_type`;
CREATE TABLE `limit_type` (
  `ID` bigint(20) NOT NULL COMMENT '主键id',
  `NAME` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `CODE` varchar(255) DEFAULT NULL,
  `DESC` varchar(255) DEFAULT NULL,
  `SYSTEM` int(20) DEFAULT NULL COMMENT '是否系统定义',
  `STATUS` varchar(20) DEFAULT NULL COMMENT '转换修改状态',
  `TDESC` varchar(255) DEFAULT NULL COMMENT '权限类型描述',
  `TYPE` varchar(20) DEFAULT NULL COMMENT '分类',
  `TABLE_ID` bigint(20) DEFAULT NULL,
  `TABLE_NAME` varchar(20) DEFAULT NULL,
  `CRTIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `CRUSER` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `MODIFY_USER` varchar(100) DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of limit_type
-- ----------------------------
INSERT INTO `limit_type` VALUES (1156097992882716674, '机构类权限', NULL, '', 'admin', 1167324599977414658, '2020-04-03 03:33:44', 1, '0', 1167324599977414658, '2022-11-10 16:31:07', '', 'admin');
INSERT INTO `limit_type` VALUES (1161946170935865345, '前台菜单类权限', NULL, '', 'admin', 1167324599977414658, '2020-04-03 03:33:44', 1, '0', 1167324599977414658, '2022-11-10 08:11:54', '', 'admin');
INSERT INTO `limit_type` VALUES (1189100639360135170, '元数据信息类权限', NULL, '', 'admin', 1167324599977414658, '2020-04-03 03:33:44', 1, '0', 1167324599977414658, '2022-11-10 08:11:55', '', 'admin');
INSERT INTO `limit_type` VALUES (1196265228058923010, '站点类权限', NULL, '', 'admin', 1167324599977414658, '2020-04-03 03:33:44', 1, '0', 1167324599977414658, '2022-11-10 08:11:57', '', 'admin');
INSERT INTO `limit_type` VALUES (1196265982328999937, '栏目类权限', NULL, '', 'admin', 1167324599977414658, '2020-04-03 03:33:44', 1, '0', 1167324599977414658, '2022-11-10 08:11:58', '', 'admin');
INSERT INTO `limit_type` VALUES (1265885567753691137, '组织设置', NULL, NULL, 'admin', 1167324599977414658, '2020-05-28 06:00:13', 1, '0', 1167324599977414658, '2022-11-10 08:12:01', NULL, 'admin');
INSERT INTO `limit_type` VALUES (1590618559775920130, '后台菜单类权限', NULL, NULL, 'admin', 1167324599977414658, '2022-11-10 16:13:12', 1, '0', 1167324599977414658, '2022-11-11 05:54:44', '', 'admin');

-- ----------------------------
-- Table structure for logininfo
-- ----------------------------
DROP TABLE IF EXISTS `logininfo`;
CREATE TABLE `logininfo`  (
  `LOGINID` bigint(20) NOT NULL COMMENT '主键id',
  `LOGINTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '登陆时间',
  `LOGINIP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登陆ip',
  `LOGOUTTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '退出时间',
  `TIMES` bigint(20) NOT NULL DEFAULT 0 COMMENT '登陆次数',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`LOGINID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of logininfo
-- ----------------------------
INSERT INTO `logininfo` VALUES (1601112463454629889, '2022-12-09 15:12:13', '192.168.0.104', NULL, 0, 'admin', 1, NULL, NULL, NULL, NULL);
INSERT INTO `logininfo` VALUES (1601118063240433666, '2022-12-09 15:34:28', '192.168.0.104', NULL, 0, 'admin', 1, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for menuman
-- ----------------------------
DROP TABLE IF EXISTS `menuman`;
CREATE TABLE `menuman`  (
  `MENUID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` int(2) NULL DEFAULT NULL COMMENT '是否前台展示',
  `natural_id` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自然Id',
  `NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `URL` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url',
  `seq` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `PARENTID` bigint(20) NULL DEFAULT NULL COMMENT '父Id',
  `STATE` int(5) NULL DEFAULT NULL COMMENT '是否活跃',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人Id',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人Id',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `photo_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片链接',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `COMPANYID` bigint(20) NULL DEFAULT NULL COMMENT '机构ID',
  `REALM_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '域名',
  `MODSTR` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块名',
  `MOD_SUFFIX` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后缀',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `siteid` bigint(20) NULL DEFAULT NULL COMMENT '站点id',
  PRIMARY KEY (`MENUID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1601100708435206147 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menuman
-- ----------------------------
INSERT INTO `menuman` VALUES (140, 1, 'msg', '消息管理', 'manage/msg/index', 179, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', NULL, '&#xe667;', 'admin', 1167323268476895233, NULL, 'message', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (160, 1, 'user', '用户管理', '', 180, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '&#xe770;', 'admin', 1167323268476895233, NULL, 'member', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (180, 1, 'limite', '权限管理', '', 181, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '&#xe672;', 'admin', 1167323268476895233, NULL, 'member', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (200, 1, 'flow', '工作流引擎', '', 182, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '&#xe628;', 'admin', 1167323268476895233, NULL, 'flowable', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (220, 1, 'menu', '菜单管理', 'simple/member/menu/list.html', 185, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, 'member', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (240, 1, 'org', '站群管理', '', 189, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '&#xe857;', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (260, 1, 'data', '元数据管理', '', 190, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '&#xe63c', 'admin', 1167323268476895233, NULL, 'metadata', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (280, 1, 'config', '系统配置', '', 191, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '&#xe665;', 'admin', 1167323268476895233, NULL, 'config', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (281, 1, 'ljconfig', '路径配置项', 'simple/config/route.html', 3, 280, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-04 09:58:22', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (285, 1, 'xtconfig', '系统配置项', 'simple/config/sysconfig.html', 7, 280, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-04 09:59:15', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (287, 1, 'yhconfig', '用户配置项', '', 9, 280, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (288, 1, 'xmconfig', '项目配置项', '', 10, 280, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (300, 1, 'task', '计划任务调度', 'simple/task/task.html', 191, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, 'task', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (320, 1, 'system', '系统信息', 'simple/config/systemmsg/list.html', 36, 280, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-04 09:59:35', '', '&#xe60b;', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (340, 1, 'log', '系统日志', '', 194, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-16 22:31:42', '', '&#xe60a;', 'admin', 1167323268476895233, NULL, 'log', '', 'admin', NULL);
INSERT INTO `menuman` VALUES (341, 1, 'realLog', '实时控制台日志', 'simple/log/reallog/list.html', 3, 340, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-17 04:00:18', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (342, 1, 'sqlLog', 'SQL日志', 'simple/log/sqllog/list.html', 4, 340, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-17 04:00:39', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (343, 1, 'safetyLog', '安全日志', 'simple/log/safety/list.html', 5, 340, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (344, 1, 'taskLog', '定时任务日志', 'simple/log/task/list.html', 6, 340, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', NULL, NULL, 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (345, 1, 'userMenuLog', '用户菜单操作日志', 'simple/log/usermenu/list.html', 7, 340, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (346, 1, 'userLoginLog', '用户登录日志', 'simple/log/userlogin/list.html', 8, 340, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (347, 1, 'downLog', '系统日志下载', '', 9, 340, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (348, 1, 'userOperationLog', '用户操作日志', 'simple/log/content/list.html', 10, 340, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (349, 1, 'contentLog', '元数据操作日志', 'simple/log/metadata/list.html', 11, 340, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2019-12-10 11:30:46', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (350, 1, 'issueTaskLog', '发布页面日志', 'simple/log/publish/list.html', 12, 340, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2019-11-28 16:47:28', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (351, 1, 'modelLog', '组件操作日志', 'simple/log/module/list.html', 13, 340, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (352, 1, 'templateLog', '模板管理日志', 'simple/log/template/list.html', 14, 340, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', NULL, NULL, 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (360, 1, 'setting', '个人设置', '', 195, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-07-25 14:34:31', '', '&#xe620;', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1122324825527554050, 1, 'webUser', '互联网用户', 'simple/member/user/ituser.html', 3, 160, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '互联网用户', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1122325053731246082, 1, 'sysUser', '系统用户', 'simple/member/user/list.html', 4, 160, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '系统用户', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1122325260447518721, 1, 'orgSet', '个人组织管理', 'simple/member/group/list.html', 5, 160, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-08-20 11:08:06', '组织管理', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1122325420292444162, 1, 'roleSet', '角色管理', 'simple/member/role/list.html', 7, 160, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '角色管理', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1122326307769421826, 1, 'dataSet', '资源管理', '', 188, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '文件管理', '&#xe655;', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1122327774010019841, 1, 'siteSet', '站点管理', 'simple/manage/site/list.html', 2, 240, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-01 14:22:06', '站点管理', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1122327938137329665, 1, 'cloumSet', '元数据栏目管理', 'simple/manage/column/list2.html', 6, 240, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-03 16:06:47', '栏目管理', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1122328303784169473, 1, 'useFunction', '应用功能管理', '', 8, 240, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '应用功能管理', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1122336229932421121, 1, 'applyStyle', '应用风格管理', 'simple/oragn/style.html', 9, 240, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-01 14:22:44', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1122336682602680321, 1, 'applyModel', '模块代码管理', 'simple/manage/component/list.html', 2, 260, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '模块代码管理', '', 'admin', 1167323268476895233, NULL, 'generator', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1141169466931601409, 1, 'metadata_design', '元数据表设计', 'simple/metadata/tableinfo.html', 3, 260, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-02 16:57:53', 'metadata_design', 'metadata_design', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1141227749453254657, 1, 'metadata_class', '分类法', 'simple/metadata/class/list.html', 10, 260, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-03 08:44:14', 'metadata_class', 'metadata_class', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1146584820767985666, 1, 'xxtz', '消息通知', 'simple/messages/msg/list.html', 2, 140, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1146632468417937410, 1, '', '发送消息', 'simple/messages/msg/send.html', 2, 140, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2022-07-06 14:20:55', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1147092190250057730, 1, '', '临时目录计划清理', 'simple/task/clean.html', 0, 300, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1147092404839038978, 1, '', '元数据同步', 'simple/task/metadata.html', 0, 300, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1147092776546648066, 1, '', '统计数据的自动执行', 'simple/task/statistics.html', 0, 300, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1147093037109395458, 1, '', '系统定时优化', 'simple/task/sys.html', 0, 300, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1147093173562687489, 1, '', '规定定时执行器', 'simple/task/task.html', 0, 300, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1148474886662950914, 1, '', '权限操作管理', 'simple/member/limit/list.html', 1, 180, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1149833443576315906, 1, '', '工作事项', 'simple/flowable/workEvents.html', 1, 200, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-30 12:14:59', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1149833582353252354, 1, '', '我的待办', 'simple/flowable/myWork.html', 1, 200, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-30 12:15:08', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1149833724867313666, 1, '', '流程统计', 'simple/flowable/total.html', 1, 200, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1149833849727549441, 1, '', '表单设计', 'simple/flowable/formDesign.html', 1, 200, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-06 22:01:54', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1149834242977103873, 1, '', '流程设计', 'simple/flowable/modeler.html', 1, 200, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1149834350540029953, 1, '', '文号管理', 'simple/flowable/doc.html', 1, 200, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-06 22:02:23', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1149834445989806082, 1, '', '文号日志', 'simple/flowable/log.html', 1, 200, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-06 22:02:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1149834616966414337, 1, '', '流程归档', 'simple/flowable/combine.html', 1, 200, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1150601498422530049, 1, 'limit', '权限分类管理', 'simple/member/limit/limitType.html', 0, 180, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1151047294250930178, 1, '', '元数据视图管理', 'simple/metadata/view.html', 4, 260, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-03 08:35:36', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1154729512799383554, 1, 'img', '图片管理', 'simple/file/imgManage/img.html', 3, 1122326307769421826, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2021-07-20 17:59:14', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1154729922029236226, 1, 'video', '视频管理', 'simple/file/videoManage/video.html', 4, 1122326307769421826, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-08-20 11:15:21', '视频', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1154730510485893122, 1, 'file', '文件管理', 'simple/file/fileManage/file.html', 5, 1122326307769421826, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-04 11:41:50', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1154730718326239234, 1, 'audio', '音频管理', 'simple/file/audioManage/audio.html', 6, 1122326307769421826, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-04 10:48:51', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1158191980104982530, 1, '', '后台菜单', 'simple/member/menu/list.html?type=1', 6, 220, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-02 14:31:20', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1158275128113283074, 1, 'frontmenu', '前台菜单', 'simple/member/menu/list.html?type=2', 5, 220, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1161170946035986434, 1, 'metaGroup', '分组管理', 'simple/metadata/group.html', 9, 260, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-03 08:43:23', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1163768421236428802, 1, 'moduleinfo', '元数据产品管理', 'simple/metadata/moduleinfo.html', 1, 260, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-02 16:41:46', '模块管理', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1167251023006314497, 1, '', '流程分类', 'simple/flowable/flowType.html', 0, 200, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2019-11-29 14:05:18', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1181443414090170369, 1, 'watermark', '水印管理', 'simple/file/watermarkManage/waterMark.html', 7, 1122326307769421826, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-09-04 10:37:11', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1183945582961508354, 1, 'basic', '基本资料', 'simple/member/user/info.html', 3, 360, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-08-18 12:47:26', '设置下三级菜单', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1183947552032047106, 1, '', '修改密码', 'simple/member/user/password.html', 4, 360, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-08-18 11:53:26', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1183949058642821122, 1, '', '草稿箱', 'simple/manage/setting/caogao.html', 5, 360, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-08-18 10:35:15', '草稿', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1183949279116410882, 1, '', '我的收藏', 'simple/manage/setting/collection.html', 6, 360, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-08-18 10:36:11', '收藏', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1183949821259563009, 1, '', '待提交资源', 'simple/manage/setting/commit.html', 9, 360, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-08-19 13:41:00', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1188654218359156738, 1, '', '便签管理', 'simple/manage/setting/note.html', 12, 360, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2021-07-19 09:23:09', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1188666718039388162, 1, '', '转码管理', 'simple/file/codeManage/code.html', 8, 1122326307769421826, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-08-18 13:52:28', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1202762960394616834, 1, '网站设置', '网站设置', 'simple/manage/setting/webset.html', 10, 360, 1, 1167324599977414658, '2019-12-06 09:33:31', 1167324599977414658, '2020-08-19 13:41:10', '网站设置', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1202765166254587905, 1, '代码自动更新', '代码自动更新', 'simple/manage/setting/autoUpdate.html', 13, 360, 1, 1167324599977414658, '2019-12-06 09:42:17', 1167324599977414658, '2020-08-19 13:40:36', '代码自动更新', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1202765631633588225, 1, '注册码管理', '注册码管理', 'simple/manage/setting/licences.html', 14, 360, 1, 1167324599977414658, '2019-12-06 09:44:08', 1167324599977414658, '2020-08-19 13:41:37', '注册码管理', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1202766026812522497, 1, '上级与下属', '上级与下属', 'simple/manage/setting/Superiors.html', 15, 360, 0, 1167324599977414658, '2019-12-06 09:45:42', 1167324599977414658, '2020-08-19 13:41:49', '上级与下属', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1213327012047962113, 1, 'apiWeb', '应用API管理', 'simple/member/menu/my-swagger-ui.html', 3, 220, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', 'api网关', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229305407526825986, 1, 'api_group', '分组', 'views/zuul/group/list.html', 1, 1229305249372205058, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229305596660576257, 1, 'api_api', 'Api', 'views/zuul/api/list.html', 0, 1229305249372205058, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229307399133360129, 1, 'api_api', 'Api管理', 'simple/gateway/api/list.html', 4, 1230009731124490242, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2022-10-26 10:02:54', '开放Api的api', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1229307505328943105, 1, 'api_group', '分组管理', 'simple/gateway/group/list.html', 3, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2022-10-26 10:04:47', '开发api的分组', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', '313131', 'admin', NULL);
INSERT INTO `menuman` VALUES (1229954277870575618, 1, 'api_ratelimit', '限流策略', 'simple/gateway/ratelimit/list.html', 5, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2022-10-26 16:25:46', '测试', '123123', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '测试模块名称', '11111', 'admin', NULL);
INSERT INTO `menuman` VALUES (1229954747204804610, 1, 'api_iprule', 'IP规则', 'simple/gateway/iprule/list.html', 6, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229954914674974721, 1, 'api_app', '应用管理', 'simple/gateway/app/list.html', 7, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229955187485089794, 1, 'api_signature', '签名密钥', 'simple/gateway/signature/list.html', 8, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229955344112984065, 1, 'api_usecaseCommodity', '实例规格', 'simple/gateway/usecaseCommodity/list.html', 9, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229955455996043265, 1, 'api_usecase', '实例管理', 'simple/gateway/usecase/list.html', 10, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229955593728598017, 1, 'api_log', '网关日志', 'simple/gateway/log/list.html', 11, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229955690075955202, 1, 'api_alarmRule', '警告规则', 'simple/gateway/alarmRule/list.html', 12, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1229955801589915650, 1, 'api_alarmLog', '警告日志', 'simple/gateway/alarmLog/list.html', 13, 1230009731124490242, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1230009731124490242, 1, '', 'Api网关', '', 160, 0, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2022-12-03 13:42:48', '', '&#xe653;', 'admin', 1167323268476895233, 'http://47.93.151.74:10010', '', '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1230010317337194498, 1, '', '首页', 'index.html', 157, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '&#xe68e;', 'admin', 1167323268476895233, NULL, '', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1234688254679224322, 1, '', '机构管理', 'simple/member/company/list.html', 1, 160, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-08-18 17:20:00', '机构管理', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1240142744467353601, 1, '', '消息源配置项', 'simple/messages/msgconfig/list.html', 0, 140, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1243386425374736385, 1, '', '可视化管理', '', 155, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1229305596660576257, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1243386721345798145, 1, 'visual', '可视化管理', '', 187, 0, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '&#xe663;', 'admin', 1167323268476895233, NULL, 'visual', NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1243388655343566850, 1, 'cpgl', '产品管理', 'simple/visualization/product/list.html', 1, 1243386721345798145, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2022-06-16 15:57:37', 'cpgl', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1243388824797642753, 1, 'zjgl', '组件管理', 'simple/visualization/subGroup/list.html', 2, 1243386721345798145, 1, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2022-06-16 15:58:06', '组件管理', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1247836832264114178, 1, '', '权限操作日志', 'simple/log/limit/list.html', 15, 340, 0, 1167324599977414658, '2020-04-21 11:41:14', 1167324599977414658, '2020-04-21 11:41:14', '', '', 'admin', 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1268372992707031042, 1, '', '资产管理', '', 196, 0, 1, 1167324599977414658, '2020-06-04 02:44:21', 1167324599977414658, '2020-08-22 20:03:40', '', '&#xe6b2;', 'admin', 1167323268476895233, NULL, 'dict', '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1268373403010625537, 1, '', '主题词管理', 'simple/dictionary/zhutici/list.html', 4, 1268372992707031042, 1, 1167324599977414658, '2020-06-04 02:45:59', 1167324599977414658, '2020-06-04 02:46:37', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1268375712197685250, 1, '', '属性词管理', 'simple/dictionary/shuxingci/list.html', 3, 1268372992707031042, 1, 1167324599977414658, '2020-06-04 02:55:10', 1167324599977414658, '2020-09-22 15:42:36', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1268375988380020738, 1, '', '反义词管理', 'simple/dictionary/fanyici/list.html', 17, 1268372992707031042, 1, 1167324599977414658, '2020-06-04 02:56:16', 1167324599977414658, '2020-09-22 15:40:41', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1268376209436618753, 1, '', '形容词管理', 'simple/dictionary/xingrongci/list.html', 16, 1268372992707031042, 1, 1167324599977414658, '2020-06-04 02:57:08', 1167324599977414658, '2020-09-22 15:46:49', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1268376547505909762, 1, '', '停用词管理', 'simple/dictionary/tingyongci/list.html', 12, 1268372992707031042, 1, 1167324599977414658, '2020-06-04 02:58:29', 1167324599977414658, '2020-06-04 02:58:29', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1268376690082885634, 1, '', '相关词管理', 'simple/dictionary/xiangguanci/list.html', 13, 1268372992707031042, 1, 1167324599977414658, '2020-06-04 02:59:03', 1167324599977414658, '2020-06-04 02:59:03', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1277081441659977730, 1, 'whstgl', '物化视图管理', 'simple/metadata/tview.html', 7, 260, 1, 1167324599977414658, '2020-06-28 11:28:37', 1167324599977414658, '2020-09-03 08:41:41', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1288017763470094337, 1, '', '句义分析展示', 'simple/dictionary/juyifenxi/list.html', 22, 1268372992707031042, 1, 1167324599977414658, '2020-07-28 15:45:40', 1167324599977414658, '2020-09-22 15:42:06', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1303470367488229378, 1, '', '满意强度管理', 'simple/dictionary/shuxingjibie/list.html', 21, 1268372992707031042, 1, 1167324599977414658, '2020-09-09 07:08:48', 1167324599977414658, '2020-12-23 09:51:51', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1305417279749672961, 1, '', '场景词管理', 'simple/dictionary/changjingci/list.html', 6, 1268372992707031042, 1, 1167324599977414658, '2020-09-14 16:05:08', 1167324599977414658, '2020-09-19 18:13:06', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1343818891876438018, 1, '', '表单设计', 'simple/viewManage/formDesign/index.html', 0, 1243386721345798145, 1, 1167324599977414658, '2020-12-29 15:19:25', 1167324599977414658, '2020-12-29 15:20:06', '', '', 'admin', 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1415577626870525954, 1, '', '全文检索模块管理', 'simple/metadata/estableinfo.html', 8, 260, 1, 1167324599977414658, '2021-07-15 15:43:01', 1167324599977414658, '2021-07-15 15:43:01', '', '', NULL, 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1417566666447073281, 1, NULL, '审核管理', 'simple/gateway/check/list.html', 14, 1230009731124490242, 1, 0, '1900-01-20 03:26:45', 1167324599977414658, '1900-01-20 08:46:00', NULL, NULL, NULL, 1167323268476895233, 'http://47.93.151.74:10010', NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1437135765569249282, 1, NULL, '主题词管理-名词库', 'simple/dictionary/zhuticiNoun/list.html', 2, 1268372992707031042, 0, 1167324599977414658, '1900-01-20 03:27:22', 1167324599977414658, '1900-01-20 03:38:54', NULL, NULL, NULL, 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1437167485047775233, 1, NULL, '主题词管理-人名库', 'simple/dictionary/zhuticiPeople/list.html', 7, 1268372992707031042, 1, 1167324599977414658, '1900-01-20 05:33:24', 1167324599977414658, '1900-01-20 05:37:25', NULL, NULL, NULL, 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1437167863600488449, 1, NULL, '主题词管理-名词库', 'simple/dictionary/zhuticiNoun/list.html', 4, 1268372992707031042, 0, 1167324599977414658, '1900-01-20 05:34:55', 1167324599977414658, '1900-01-20 09:57:24', NULL, NULL, NULL, 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1437168164013318146, 1, NULL, '主题词管理-机构名录库', 'simple/dictionary/zhuticiOrgan/list.html', 13, 1268372992707031042, 1, 1167324599977414658, '1900-01-20 05:36:06', 1167324599977414658, '1900-01-20 05:39:35', NULL, NULL, NULL, 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1437168247119257602, 1, NULL, '主题词管理-地名库', 'simple/dictionary/zhuticiPlace/list.html', 12, 1268372992707031042, 1, 1167324599977414658, '1900-01-20 05:36:26', 1167324599977414658, '1900-01-20 05:38:20', NULL, NULL, NULL, 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1437473789024813057, 1, NULL, '主题词管理-名词库', 'simple/dictionary/zhuticiNoun/list.html', 6, 1268372992707031042, 1, 1167324599977414658, '1900-01-20 01:50:33', 1167324599977414658, '1900-01-20 01:50:46', NULL, NULL, NULL, 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1533656086901153794, 1, 'api_jyrule', '校验规则维护管理', 'simple/gateway/rule/list.html', 5, 1230009731124490242, 1, 1167324599977414658, '2022-06-06 03:44:40', 1167324599977414658, '2022-06-06 03:44:40', NULL, NULL, NULL, 1167323268476895233, NULL, NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1540219593675173890, 1, 'api_jyruletest', '系统参数维护管理', 'simple/gateway/maintain/list.html', 8, 1230009731124490242, 1, 1167324599977414658, '2022-06-24 06:25:42', 1167324599977414658, '2022-06-26 07:10:36', NULL, '', NULL, 1167323268476895233, '', NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1540220210237861890, 1, 'api_ruletest', '数据校验接口测试', 'simple/gateway/rule/runtest.html', 6, 1230009731124490242, 1, 1167324599977414658, '2022-06-24 06:28:09', 1167324599977414658, '2022-06-26 07:11:38', NULL, '', NULL, 1167323268476895233, '', NULL, NULL, 'admin', NULL);
INSERT INTO `menuman` VALUES (1601100666127261698, 2, '', '样例菜单', '', 1, 0, 1, 1, '2022-12-09 14:25:20', 1, '2022-12-09 14:25:20', '', '', NULL, 1167323268476895233, NULL, NULL, '', 'admin', NULL);
INSERT INTO `menuman` VALUES (1601100708435206146, 2, '', '样例子菜单', '', 1, 1601100666127261698, 1, 1, '2022-12-09 14:25:31', 1, '2022-12-09 14:25:31', '', '', NULL, 1167323268476895233, NULL, NULL, '', 'admin', NULL);

-- ----------------------------
-- Table structure for metadata_group
-- ----------------------------
DROP TABLE IF EXISTS `metadata_group`;
CREATE TABLE `metadata_group`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组名',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `PARENTID` decimal(20, 0) NULL DEFAULT NULL,
  `groupdesc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组描述',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of metadata_group
-- ----------------------------
INSERT INTO `metadata_group` VALUES (1601108185201635329, '样例元数据字段分组', 'admin', 1, '2022-12-09 14:55:13', NULL, NULL, 0, '样例元数据字段分组', NULL);
INSERT INTO `metadata_group` VALUES (1601108201261625346, '样例元数据字段分组', 'admin', 1, '2022-12-09 14:55:17', NULL, NULL, 1601108185201635329, '样例元数据字段分组', NULL);

-- ----------------------------
-- Table structure for metadata_pic
-- ----------------------------
DROP TABLE IF EXISTS `metadata_pic`;
CREATE TABLE `metadata_pic`  (
  `ID` bigint(20) NOT NULL,
  `TABLEID` bigint(20) NULL DEFAULT NULL,
  `TABLENAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DATAID` bigint(20) NULL DEFAULT NULL,
  `PICID` bigint(20) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of metadata_pic
-- ----------------------------

-- ----------------------------
-- Table structure for module_viewinfo
-- ----------------------------
DROP TABLE IF EXISTS `module_viewinfo`;
CREATE TABLE `module_viewinfo`  (
  `moduleid` decimal(20, 0) NOT NULL COMMENT '主键pk',
  `moduleinfoid` decimal(20, 0) NOT NULL COMMENT '模板id',
  `viewid` decimal(20, 0) NOT NULL COMMENT '视图id',
  `moduleviewname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视图中文名称',
  `modulename` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视图英文名称',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人名称',
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`moduleid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模板视图表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of module_viewinfo
-- ----------------------------

-- ----------------------------
-- Table structure for moduleinfo
-- ----------------------------
DROP TABLE IF EXISTS `moduleinfo`;
CREATE TABLE `moduleinfo`  (
  `MODULEINFOID` decimal(20, 0) NOT NULL COMMENT '主键pk',
  `MODULENAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `MODULEDESC` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板描述说明',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' \r\n模板创建者\r\n',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT ' \r\n模板创建时间\r\n',
  `CRNUMBER` decimal(20, 0) NULL DEFAULT NULL COMMENT ' \r\n模板创建人ID\r\n',
  `ENGLISHNAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板英文名称',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人id',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人名称',
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `moduleorder` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`MODULEINFOID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of moduleinfo
-- ----------------------------
INSERT INTO `moduleinfo` VALUES (1601107447104794626, '样例元数据模块', '样例元数据模块', 'admin', '2022-12-09 14:52:17', 1, 'yjysjmk', NULL, NULL, NULL, NULL, 1);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `ID` bigint(20) NOT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PERMISSION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------

-- ----------------------------
-- Table structure for plantask
-- ----------------------------
DROP TABLE IF EXISTS `plantask`;
CREATE TABLE `plantask`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `pattern` int(1) NULL DEFAULT NULL COMMENT '运行模式',
  `fileType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '文件类型',
  `tasktype` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务类型',
  `opeparam` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作参数',
  `startTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '执行时间',
  `endTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
  `interval` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '间隔时间',
  `deldate` bigint(20) NULL DEFAULT NULL COMMENT '删除几天前的文件',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '计划任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of plantask
-- ----------------------------
INSERT INTO `plantask` VALUES (6, '规定定时', '测试', NULL, '', '规定定时执行器', '/test', '2020-04-21 11:50:42', '2020-04-21 11:50:42', NULL, NULL, 'admin', 1, '2020-04-21 11:50:42', 1167324599977414658, '2020-04-21 11:50:42', 'admin');
INSERT INTO `plantask` VALUES (7, '系统定时', '测试', NULL, '', '系统定时优化', '/test', '2020-04-21 11:50:42', '2020-04-21 11:50:42', NULL, NULL, 'admin', 2, '2020-04-21 11:50:42', 1167324599977414658, '2020-04-21 11:50:42', 'admin');
INSERT INTO `plantask` VALUES (8, '统计数据', '测试', NULL, '', '统计数据的自动', '/test', '2020-04-21 11:50:42', '2020-04-21 11:50:42', NULL, NULL, 'admin', 3, '2020-04-21 11:50:42', 1167324599977414658, '2020-04-21 11:50:42', 'admin');
INSERT INTO `plantask` VALUES (9, '临时目录', '测试', NULL, '', '临时目录计划清理', '/test', '2020-04-21 11:50:42', '2020-04-21 11:50:42', NULL, NULL, 'admin', 4, '2020-04-21 11:50:42', 1167324599977414658, '2020-04-21 11:50:42', 'admin');
INSERT INTO `plantask` VALUES (10, '元数据同步', '测试', NULL, '', '元数据同步', '/test', '2020-04-21 11:50:42', '2020-04-21 11:50:42', NULL, NULL, 'admin', 4, '2020-04-21 11:50:42', 1167324599977414658, '2020-04-21 11:50:42', 'admin');
INSERT INTO `plantask` VALUES (1288672483926441985, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', 1167324599977414658, '2020-07-30 11:07:17', 1167324599977414658, '2020-07-30 11:07:17', 'admin');

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `START_TIME` datetime NOT NULL,
  `END_TIME` datetime NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` datetime NULL DEFAULT NULL,
  `id` decimal(20, 0) NOT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for role_user
-- ----------------------------
DROP TABLE IF EXISTS `role_user`;
CREATE TABLE `role_user`  (
  `ROLEUSERID` bigint(20) NOT NULL COMMENT '主键id',
  `ROLEID` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `USERID` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `ISLEADER` int(5) NULL DEFAULT NULL COMMENT '是否为组长',
  `USERORDER` bigint(20) NULL DEFAULT NULL COMMENT '排序字段',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `JOINTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`ROLEUSERID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_user
-- ----------------------------
INSERT INTO `role_user` VALUES (1, 1, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `role_user` VALUES (1601099019565146113, 10, 10, 1, NULL, 'admin', 1, '2022-12-09 06:18:34', NULL, '2022-12-09 06:18:34', '2022-12-09 06:18:34', NULL);

-- ----------------------------
-- Table structure for roleinfo
-- ----------------------------
DROP TABLE IF EXISTS `roleinfo`;
CREATE TABLE `roleinfo`  (
  `ROLEID` bigint(20) NOT NULL COMMENT '主键id',
  `ROLENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `ROLETYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色类型',
  `ROLEPATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色结构层级',
  `ROLEDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述说明',
  `PARENTID` bigint(20) NULL DEFAULT NULL COMMENT '父角色编号',
  `ROLEORDER` bigint(10) NULL DEFAULT NULL COMMENT '显示顺序字段',
  `STATUS` int(2) NULL DEFAULT NULL COMMENT '状态',
  `ADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `MOBILE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `EMAIL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `QQ` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'qq',
  `PHONE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `ROLECODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `COMPANYID` bigint(20) NULL DEFAULT NULL COMMENT '所属机构',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`ROLEID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色基本信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roleinfo
-- ----------------------------
INSERT INTO `roleinfo` VALUES (1, '管理员', '系统级角色', '11111', '管理员', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 'sss', 'admin', 1167324599977414658, '2022-12-09 06:08:42', 1167324599977414658, '2022-12-09 06:08:42', 1167323268476895233, 'admin');
INSERT INTO `roleinfo` VALUES (10, '审计员', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 'code', 'admin', 1167324599977414658, '2022-12-09 06:08:48', 1167324599977414658, '2022-12-09 06:08:48', 1167323268476895233, 'admin');

-- ----------------------------
-- Table structure for subscribeclass
-- ----------------------------
DROP TABLE IF EXISTS `subscribeclass`;
CREATE TABLE `subscribeclass`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type` int(20) NULL DEFAULT NULL COMMENT '订阅状态（1为订阅，0为取消）',
  `userid` bigint(20) NULL DEFAULT NULL COMMENT '订阅者id',
  `classid` bigint(20) NULL DEFAULT NULL COMMENT '分类法id',
  `subscribetime` datetime NULL DEFAULT NULL COMMENT '订阅时间',
  `classname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类法名称',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订阅者名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of subscribeclass
-- ----------------------------
INSERT INTO `subscribeclass` VALUES (1, 1, NULL, 1, NULL, NULL, '张三');
INSERT INTO `subscribeclass` VALUES (2, 1, NULL, 1531557932954210305, NULL, NULL, NULL);
INSERT INTO `subscribeclass` VALUES (3, 1, NULL, 1585921090560929793, '2022-12-07 08:35:04', NULL, '王五');

-- ----------------------------
-- Table structure for subscrip
-- ----------------------------
DROP TABLE IF EXISTS `subscrip`;
CREATE TABLE `subscrip`  (
  `id` bigint(20) NOT NULL,
  `nauser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` bigint(20) NULL DEFAULT NULL,
  `crtime` datetime NULL DEFAULT NULL,
  `pid` bigint(20) NULL DEFAULT NULL,
  `type` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of subscrip
-- ----------------------------
INSERT INTO `subscrip` VALUES (1, '王五', 1, '2022-12-06 11:52:37', 1585921090560929793, NULL);

-- ----------------------------
-- Table structure for swaggerconfiguration
-- ----------------------------
DROP TABLE IF EXISTS `swaggerconfiguration`;
CREATE TABLE `swaggerconfiguration`  (
  `basePackagename` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `groupName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `beanname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL,
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of swaggerconfiguration
-- ----------------------------
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.config', '配置（config）', 'createConfigApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 06:52:14');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.log', '日志（log）', 'createLogApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:29:48');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.manage', '站群（manage）', 'createManageApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:29:56');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.demo', '示例（demo）', 'createDemoApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 08:06:46');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.metadata', '元数据（metadata）', 'createMetadataApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:30:07');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.resources', '资源（resources）', 'createResourcesApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:30:18');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.flowable', '工作流（flowable）', 'createFlowableApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:30:33');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.generator', '生成代码（generator）', 'createGeneratorApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:30:57');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.member', '用户（member）', 'createMemberApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:19:24');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.message', '消息（message）', 'createMessageApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:31:07');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.visual', '可视化（visual）', 'createVisualApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:31:16');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.dict', '词典（dict）', 'createDictApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:31:40');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.media', '融媒（media）', 'createTestMediaApi', 'admin', 'admin', '2020-04-21 11:41:14', 1167324599977414658, '86', 1167324599977414658, '2022-10-08 03:31:28');
INSERT INTO `swaggerconfiguration` VALUES ('com.jnetdata.msp.yjysjmk', 'yjysjmk', 'createYjysjmkApi', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_job`;
CREATE TABLE `sys_quartz_job`  (
  `id` decimal(20, 0) NOT NULL,
  `create_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `del_flag` decimal(20, 0) NULL DEFAULT NULL,
  `job_class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cron_expression` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parameter` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` decimal(20, 0) NULL DEFAULT NULL,
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人Id',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人Id',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  `cruser` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `old_job_class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------
INSERT INTO `sys_quartz_job` VALUES (1169524268174659586, NULL, NULL, 0, 'com.jnetdata.simple.manage.plantask.job.SampleJob', '0/10 * * * * ? ', 'execute', '测试用例2', -1, 1167324599977414658, '2019-09-05 16:14:49', 1167324599977414658, '2020-06-23 01:51:22', 'admin', 'com.jnetdata.simple.manage.plantask.job.SampleJob', 'admin');
INSERT INTO `sys_quartz_job` VALUES (1170147233513816066, NULL, NULL, 0, 'com.jnetdata.simple.manage.plantask.job.SampleParamJob', '0/10 * * * * ? ', 'execute', '测试用例', -1, 1167324599977414658, '2019-09-07 09:30:15', 1167324599977414658, '2020-06-23 01:52:14', 'admin', 'com.jnetdata.simple.manage.plantask.job.SampleParamJob', 'admin');
INSERT INTO `sys_quartz_job` VALUES (1176811337502670850, NULL, NULL, 0, 'com.jnetdata.simple.generator.controller.JgitTest', '0/59 * * * * ? ', 'test', 'sad', -1, 1167324599977414658, '2019-09-25 18:51:02', 1167324599977414658, '2020-06-23 18:03:48', 'admin', 'com.jnetdata.simple.generator.controller.JgitTest', 'admin');
INSERT INTO `sys_quartz_job` VALUES (1281475494155444225, NULL, NULL, 0, 'com.jnetdata.msp.media.service.impl.JobApiBean', '0/10 * * * * ?', 'runJob', '定时运行api', -1, NULL, '2020-07-10 14:29:01', 1167324599977414658, '2020-07-17 10:17:41', NULL, 'com.jnetdata.msp.media.service.impl.JobApiBean', 'admin');

-- ----------------------------
-- Table structure for sysappinfo
-- ----------------------------
DROP TABLE IF EXISTS `sysappinfo`;
CREATE TABLE `sysappinfo`  (
  `ID` bigint(20) NOT NULL COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `APPNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用名',
  `APPVERSION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本',
  `APPUSERS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '使用对象',
  `APPFRAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '框架',
  `MANUAL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手册',
  `DESCRIPTION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '特色描述',
  `COPYRIGHT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版权',
  `FRONTURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前台地址',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `LOGINERRORTIME` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sysappinfo
-- ----------------------------
INSERT INTO `sysappinfo` VALUES (1171988080056377346, 'admin', 1167324599977414658, '2022-10-21 08:38:02', 1167324599977414658, '2022-10-21 16:39:40', '一体化运营平台', '1.24', '系统用户,互联网用户', 'SpringBoot,Mybatis plus', '开发者手册,使用手册', '方便快捷', '北京中科聚网信息技术有限公司   电话：010-56181910', '/feiyi/index.html', 'admin', -1);

-- ----------------------------
-- Table structure for sysconfig
-- ----------------------------
DROP TABLE IF EXISTS `sysconfig`;
CREATE TABLE `sysconfig`  (
  `CONFIGID` bigint(20) NOT NULL COMMENT '主键id',
  `CTYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  `CKEY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `CVALUE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `CDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `MSGTYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息类型',
  `IFENCRYPT` int(1) NULL DEFAULT 0 COMMENT '是否加密 0不加密 1 加密',
  `GROUPID` bigint(20) NULL DEFAULT NULL COMMENT '组织Id',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `SITEID` bigint(20) NULL DEFAULT NULL COMMENT '站点信息id',
  `MSGSIGN` int(20) NULL DEFAULT NULL COMMENT '消息源类型，0：消息配置 1：测试配置 2：消息短信 3：消息微信 4：系统消息 5：app消息',
  `MARK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块标识',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `MAIL_ACCOUNT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件账号',
  `MAIL_PASSWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件账号密码',
  `MAIL_TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件类型',
  `MAIL_SMTP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务器地址',
  `IFSLL` int(1) NULL DEFAULT NULL COMMENT '是否SSL 0不 1 SSL',
  `MAIL_PORT` int(100) NULL DEFAULT NULL COMMENT '端口号',
  `LOGINNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信账号用户名',
  `PASSWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信账号密码',
  `CORPID` int(100) NULL DEFAULT NULL COMMENT '企业编号',
  PRIMARY KEY (`CONFIGID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sysconfig
-- ----------------------------
INSERT INTO `sysconfig` VALUES (3, '2', '测试文档', '/b/test', '测试文档配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (4, '2', '测试文档', '/b/test', '测试文档配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (5, '3', '测试栏目', '/b/test', '测试栏目配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (6, '3', '测试栏目', '/b/test', '测试栏目配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (7, '4', '测试日志', '/c/test', '测试日志配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (8, '4', '测试日志', '/c/test', '测试日志配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (9, '5', '测试系统', '/c/test', '测试系统配置', NULL, 0, NULL, 1167324599977414658, '2020-05-14 14:17:57', 1167324599977414658, '2020-05-14 01:17:57', '武磊', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (11, '6', '测试任务', '/c/test', '测试任务配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (12, '6', '测试任务', '/c/test', '测试任务配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (13, '7', '测试用户', '/c/test', '测试用户配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (14, '7', '测试用户', '/c/test', '测试用户配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (15, '8', '测试项目', '/c/test', '测试项目配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (16, '8', '测试项目', '/c/test', '测试项目配置', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (17, '10', '图片板式', '横图', '测试图片板式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (18, '11', '图片格式', 'jpg', '测试图片格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (29, '10', '图片板式', '竖图', '测试图片板式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (30, '11', '图片格式', 'png', '测试图片格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (31, '11', '图片格式', 'tif', '测试图片格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (32, '11', '图片格式', 'gif', '测试图片格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (33, '11', '图片格式', 'bmp', '测试图片格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (34, '12', '图片版权', '自主', '测试图片版权归属', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (35, '12', '图片版权', '经授权', '测试图片版权归属', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (36, '13', '视频分辨率', '360p', '测试视频分辨率', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (37, '13', '视频分辨率', '720p', '测试视频分辨率', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (38, '13', '视频分辨率', '1080p', '测试视频分辨率', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (39, '14', '视频格式', 'mp4', '测试视频格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (40, '14', '视频格式', 'avi', '测试视频格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (41, '14', '视频格式', 'mpg', '测试视频格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (42, '14', '视频格式', 'mkv', '测试视频格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (43, '15', '视频时长', '5以内', '测试视频时长', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (44, '15', '视频时长', '5~20', '测试视频时长', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (45, '15', '视频时长', '20~60', '测试视频时长', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (46, '15', '视频时长', '60以上', '测试视频时长', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (47, '16', '视频版权', '自主', '测试视频版权', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (48, '16', '视频版权', '经授权', '测试视频版权', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (49, '17', '文件格式', 'doc', '测试文件格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (50, '17', '文件格式', 'xlsx', '测试文件格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (51, '17', '文件格式', 'txt', '测试文件格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (52, '17', '文件格式', 'ppt', '测试文件格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (53, '18', '文件版权', '自主', '测试文件版权', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (54, '18', '文件版权', '经授权', '测试文件版权', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (55, '19', '音频时长', '5以内', '测试音频时长', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (56, '19', '音频时长', '5~30', '测试音频时长', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (57, '19', '音频时长', '30以上', '测试音频时长', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (58, '20', '音频格式', 'mp3', '测试音频格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (59, '20', '音频格式', 'wav', '测试音频格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (60, '20', '音频格式', 'wma', '测试音频格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (61, '20', '音频格式', 'asf', '测试音频格式', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (62, '21', '音频版权', '自主', '测试音频版权', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (63, '21', '音频版权', '经授权', '测试音频版权', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (64, '22', '消息类型', '短信', '测试消息类型', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (65, '22', '消息类型', '微信', '测试消息类型', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (66, '22', '消息类型', '系统消息', '测试消息类型', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (67, '22', '消息类型', 'APP消息', '测试消息类型', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (68, '22', '消息类型', '邮件', '测试消息类型', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1149220029808603137, '0', '1', '2', '3', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1149230084633227265, '0', '', '', '', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1163276126821072898, '9', '短信', '短信', '测试短信服务', '短信', 1, 1121653304744247297, 1167324599977414658, '2022-12-08 13:03:29', 1167324599977414658, '2022-12-08 21:04:03', 'admin', NULL, 2, NULL, 'admin', '', '', '', '', 0, 0, 'kfpthy', 'kfpt123456', 2063030);
INSERT INTO `sysconfig` VALUES (1163276358409568257, '9', '系统消息', '系统消息', '系统消息', '系统消息', 1, 1121653304744247297, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin', NULL, 4, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1163276455893581826, '9', 'APP消息', 'APP消息', 'app消息', 'APP消息', 1, 1121653304744247297, 1167324599977414658, '2022-11-01 07:44:31', 1167324599977414658, '2022-11-01 15:44:32', 'admin', NULL, 5, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1166518270497435650, '5', '上传文件配置', 'D:\\sysconfig\\upload\\size.xml', '配置上传文件大小', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', '赵飞', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1166522751989624834, '5', ' 系统默认语言', '中文（简体）', ' 系统默认语言', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', '刘旭东', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1166522860924088322, '5', '系统默认字符编码', 'GBK', '系统默认字符编码', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', '马宏宇', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1166522997016670210, '5', ' 容器类型', 'Apache Tomcat/7.0.79', ' 容器类型', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', '范伟', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1166523251959050242, '5', ' Java安装目录', 'C:\\Program Files\\Java\\jre7', '\nJava安装目录', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', '薛春霞', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1166523589088817153, '5', '操作系统的名称', 'Windows Server 2008 R2', '操作系统的名称', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', '谢晓花', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1166523761822838786, '5', '操作系统的版本', '6.1', '操作系统的版本', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', '蔡志刚', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1166524145656180738, '5', '数据库类型', 'Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 - 64bit Production With the Partitioning, OLAP, Data Mining and Real Application Testing options', '数据库类型', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', '黄汉杰', NULL, NULL, NULL, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1167395202814476290, '1', '图片上传路径', 'webpic', '图片上传路径', NULL, 0, NULL, 1167324599977414658, '2022-11-01 06:58:28', 1167324599977414658, '2022-11-01 14:58:29', 'admin', NULL, NULL, 'dir_pic', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1167404565587406850, '1', '文件上传路径', 'webfile', '文件上传路径', NULL, 0, NULL, 1167324599977414658, '2020-04-21 12:00:03', 1167324599977414658, '2020-04-21 12:00:03', 'admin', NULL, NULL, 'dir_file', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1167404682386190338, '1', '视频上传路径', 'webvideo', '视频上传路径', NULL, 0, NULL, 1167324599977414658, '2020-04-21 12:00:03', 1167324599977414658, '2020-04-21 12:00:03', 'admin', NULL, NULL, 'dir_video', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1167404882748092418, '1', '音频上传路径', 'webaudio', '音频上传路径', NULL, 0, NULL, 1167324599977414658, '2020-04-21 12:00:03', 1167324599977414658, '2020-04-21 12:00:03', 'admin', NULL, NULL, 'dir_audio', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1186532739490971650, '9', '样例qq邮件', 'qq邮件', '邮件', '邮件', 1, NULL, 1167324599977414658, '2022-12-09 06:12:53', 1, '2022-12-09 14:13:28', 'admin', NULL, NULL, NULL, 'admin', '1111111111@qq.com', '1111111111', '@qq.com', 'smtp.qq.com', 0, 0, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1242977855679979521, '1', '上传路径根目录', '/opt/fastdev/upload', '本机 E:\\upload\n/data/upload\nD:\\upload\n/root/fastdev_boot/upload\n/opt/fastdev/upload', NULL, 0, NULL, 1167324599977414658, '2022-11-25 10:14:58', 1167324599977414658, '2022-11-25 18:14:59', 'admin', NULL, NULL, 'dir_upload_base', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1242980241735639041, '1', '头像上传路径', 'headimg', '', NULL, 0, NULL, 1167324599977414658, '2022-11-04 01:20:17', 1167324599977414658, '2022-11-04 09:20:22', 'admin', NULL, NULL, 'dir_headimg', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1248124023707705346, '1', '前端部署路径', '/opt/fastdev/web', '/data/web\n/root/fastdev_boot/web\nF:\\work\\upload\n/opt/fastdev/web', NULL, 0, NULL, 1167324599977414658, '2022-11-25 10:19:15', 1167324599977414658, '2022-11-25 18:19:16', 'admin', NULL, NULL, 'dir_front', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1250686175006437378, '1', '水印上传路径', 'watermaker', '', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:21:15', 1167324599977414658, '2020-04-21 11:21:15', 'admin', NULL, NULL, 'dir_watermaker', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1251071498488119298, '1', '缩略图路径', 'thumbnail', 'thumbnail', NULL, 0, NULL, 1167324599977414658, '2020-04-21 11:21:15', 1167324599977414658, '2020-04-21 11:21:15', 'admin', NULL, NULL, 'dir_thumbnail', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1304315555083210753, '1', '前端发布路径', 'pub', '前端发布路径', NULL, 0, NULL, 1167324599977414658, '2020-09-11 15:07:16', 1167324599977414658, '2020-09-11 15:07:16', 'admin', NULL, NULL, 'dir_pub', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1304315658787377154, '1', '前端预览路径', 'preview', '前端预览路径', NULL, 0, NULL, 1167324599977414658, '2020-09-11 15:07:41', 1167324599977414658, '2020-09-11 15:07:41', 'admin', NULL, NULL, 'dir_preview', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1399634059026452482, '1', 'ffmpeg路径', 'c:\\Program\\ffmpeg-4.4-essentials_build\\bin', 'ffmpeg路径', NULL, 0, NULL, 1167324599977414658, '2021-06-01 15:48:58', NULL, NULL, 'admin', NULL, NULL, 'dir_ffmpeg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1540181925480820738, '1', '测试路径', 'cs', '测试路径', NULL, 0, NULL, 1167324599977414658, '2022-06-24 11:56:01', NULL, NULL, 'admin', NULL, NULL, 'ceshi', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1540182724432818178, '1', 'ceshi', 'ceshi', 'ceshi', NULL, 0, NULL, 1167324599977414658, '2022-10-21 08:32:00', 1167324599977414658, '2022-10-21 16:33:38', 'admin', NULL, NULL, 'ceshi', 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1597419973793746945, '9', '微信', '微信', '微信', '微信', 1, NULL, 1167324599977414658, '2022-11-29 02:39:24', 1167324599977414658, '2022-11-29 10:39:49', 'admin', NULL, NULL, NULL, 'admin', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1600041215632506882, '9', '样例163邮件', '样例163邮件', '样例163邮件', '邮件', 1, NULL, 1167324599977414658, '2022-12-09 06:13:23', 1, '2022-12-09 14:13:58', 'admin', NULL, NULL, NULL, 'admin', '1111111@163.com', '11111111', '@163.com', 'smtp.163.com', 0, 0, NULL, NULL, NULL);
INSERT INTO `sysconfig` VALUES (1600050858229997570, '0', 'dbSource', 'localhost:9200', 'dbSource', NULL, 0, NULL, 1167324599977414658, '2022-12-06 08:54:28', NULL, '2022-12-06 08:54:28', 'admin', NULL, NULL, 'dbSource', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sysconfigtype
-- ----------------------------
DROP TABLE IF EXISTS `sysconfigtype`;
CREATE TABLE `sysconfigtype`  (
  `TYPEID` bigint(20) NOT NULL COMMENT '主键id',
  `TYPENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名',
  `TYPEDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型描述',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`TYPEID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sysconfigtype
-- ----------------------------
INSERT INTO `sysconfigtype` VALUES (1, '路径配置', '', 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (2, '文档配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (3, '栏目配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (4, '日志配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (5, '系统配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (6, '定时任务', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (7, '用户配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (8, '项目配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (9, '消息源配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (10, '图片板式配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (11, '图片格式配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (12, '图片归属权配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (13, '视频分辨率配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (14, '视频格式配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (15, '视频时长配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (16, '视频版权配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (17, '文件格式配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (18, '文件版权配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (19, '音频时长配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (20, '音频格式配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (21, '音频版权配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');
INSERT INTO `sysconfigtype` VALUES (22, '消息类型配置', NULL, 'admin', 1167324599977414658, '2020-04-21 11:50:43', 1167324599977414658, '2020-04-21 11:50:43', 'admin');

-- ----------------------------
-- Table structure for t_column
-- ----------------------------
DROP TABLE IF EXISTS `t_column`;
CREATE TABLE `t_column`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `mark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_column
-- ----------------------------

-- ----------------------------
-- Table structure for t_content_log
-- ----------------------------
DROP TABLE IF EXISTS `t_content_log`;
CREATE TABLE `t_content_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `handle_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `content_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容类型',
  `columnid` bigint(20) NULL DEFAULT NULL COMMENT '栏目id',
  `siteid` bigint(20) NULL DEFAULT NULL COMMENT '网站id',
  `column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `RESULT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作明细',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作状态',
  `column_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目',
  `c_detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作状态',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内容操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_content_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_document
-- ----------------------------
DROP TABLE IF EXISTS `t_document`;
CREATE TABLE `t_document`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `mark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_document
-- ----------------------------

-- ----------------------------
-- Table structure for t_item_config
-- ----------------------------
DROP TABLE IF EXISTS `t_item_config`;
CREATE TABLE `t_item_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `mark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_item_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_limit_type
-- ----------------------------
DROP TABLE IF EXISTS `t_limit_type`;
CREATE TABLE `t_limit_type`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `limitId` bigint(20) NULL DEFAULT NULL COMMENT '权限id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
  `type` int(5) NULL DEFAULT NULL COMMENT '类型(1：新增2：删除3：编辑4：查看)',
  `status` int(5) NULL DEFAULT 0 COMMENT '状态',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_limit_type
-- ----------------------------

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `mark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_metadata_log
-- ----------------------------
DROP TABLE IF EXISTS `t_metadata_log`;
CREATE TABLE `t_metadata_log`  (
  `ID` bigint(10) NOT NULL COMMENT '主键id',
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `HANDLETYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `RESULT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作结果',
  `TITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据标题',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `ADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `DETAIL` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作明细',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_metadata_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_model_log
-- ----------------------------
DROP TABLE IF EXISTS `t_model_log`;
CREATE TABLE `t_model_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `columnname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目',
  `model_id` bigint(20) NULL DEFAULT NULL COMMENT '组件id',
  `model_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件类型',
  `handle_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作明细',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组件操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_model_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_power_log
-- ----------------------------
DROP TABLE IF EXISTS `t_power_log`;
CREATE TABLE `t_power_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `powername` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `power_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限类型',
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作明细',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_power_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_publish_log
-- ----------------------------
DROP TABLE IF EXISTS `t_publish_log`;
CREATE TABLE `t_publish_log`  (
  `ID` bigint(20) NOT NULL COMMENT '主键id',
  `COLUMNNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目名',
  `PATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `PATHNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `ADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_publish_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_route
-- ----------------------------
DROP TABLE IF EXISTS `t_route`;
CREATE TABLE `t_route`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `mark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_route
-- ----------------------------

-- ----------------------------
-- Table structure for t_task
-- ----------------------------
DROP TABLE IF EXISTS `t_task`;
CREATE TABLE `t_task`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `mark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `CPYTEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_task
-- ----------------------------

-- ----------------------------
-- Table structure for t_task_log
-- ----------------------------
DROP TABLE IF EXISTS `t_task_log`;
CREATE TABLE `t_task_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原始消息',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_task_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_template_log
-- ----------------------------
DROP TABLE IF EXISTS `t_template_log`;
CREATE TABLE `t_template_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `column_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目名称',
  `template_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板类型',
  `template_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `handle_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `handle_result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作结果',
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作明细',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `ADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模板管理日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_template_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_uesrmenu_log
-- ----------------------------
DROP TABLE IF EXISTS `t_uesrmenu_log`;
CREATE TABLE `t_uesrmenu_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原始消息',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后创建人姓名',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户菜单操作日志表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for tableinfo
-- ----------------------------
DROP TABLE IF EXISTS `tableinfo`;
CREATE TABLE `tableinfo`  (
  `TABLEINFOID` decimal(20, 0) NOT NULL COMMENT '主键pk',
  `TABLENAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据名称',
  `ANOTHERNAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示名称',
  `TABLETYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '元数据类型',
  `TABLEDESC` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述说明',
  `OWNERTYPE` decimal(5, 0) NULL DEFAULT NULL COMMENT '所属对象类型',
  `OWNERID` decimal(20, 0) NULL DEFAULT NULL COMMENT '所属对象编号',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' \r\n对象创建者\r\n',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT ' \r\n对象创建时间\r\n',
  `CRNUMBER` decimal(20, 0) NULL DEFAULT NULL COMMENT ' \r\n创建人ID\r\n',
  `LISTJSP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列表页jsp',
  `FOLDERPATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页面路径',
  `PK` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '该元数据表的主键名\r\n\r\n',
  `ADDJSP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '新增jsp',
  `EDITJSP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改jsp',
  `SHOWJSP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示jsp',
  `MODELFILENAME` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '模板文件名',
  `MODELFILEPATH` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '模板文件路径',
  `SHOW_LIST` int(1) NOT NULL DEFAULT 0 COMMENT '列表展示{0:不展示,1:展示}',
  `SHOW_SEARCH` int(1) NOT NULL DEFAULT 0 COMMENT '搜索展示{0:不展示,1:展示}',
  `SHOW_DETAIL` int(1) NOT NULL DEFAULT 1 COMMENT '详情展示{0:不展示,1:展示}',
  `generatetime` datetime NULL DEFAULT NULL COMMENT '生成时间',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  `introducetime` datetime NULL DEFAULT NULL COMMENT '引入元数据表时间',
  `introduceuser` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '引入元数据表的用户名称',
  `generateuser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成人名称',
  `visualid` bigint(20) NULL DEFAULT NULL COMMENT '可视化组件id',
  `visualTypeid` bigint(20) NULL DEFAULT NULL COMMENT '可视化组件类型id',
  `VIEWSQL` varchar(2000) NULL DEFAULT NULL COMMENT '视图sql'
  PRIMARY KEY (`TABLEINFOID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tableinfo
-- ----------------------------
INSERT INTO `tableinfo` VALUES (1601107907144445953, 'JMETA_YLTABLE', '产品表(样例元数据表)', 'table', NULL, 0, 0, 'admin', '2022-12-09 14:54:07', 1, NULL, NULL, 'ID', NULL, NULL, NULL, '', NULL, 0, 0, 1, NULL, 'admin', 1, '2022-12-09 14:54:07', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for tabletemplate
-- ----------------------------
DROP TABLE IF EXISTS `tabletemplate`;
CREATE TABLE `tabletemplate`  (
  `TEMPID` decimal(20, 0) NOT NULL COMMENT '主键pk',
  `SITEID` decimal(20, 0) NULL DEFAULT NULL COMMENT ' 站点ID',
  `TEMPNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ' 模板名称',
  `TEMPDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 模板描述',
  `TEMPEXT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '默认扩展名 例如.html\r\n \r\n',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 创建者\r\n \r\n',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间\r\n \r\n',
  `CRNUMBER` decimal(20, 0) NULL DEFAULT NULL COMMENT '创建人ID\r\n \r\n',
  `modify_by` decimal(20, 0) NULL DEFAULT NULL COMMENT '最后修改id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `TEMPTYPE` decimal(5, 0) NULL DEFAULT NULL COMMENT '模板类型',
  `OUTPUTFILENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '输出文件名',
  `LASTMODIFIEDUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后操作用户',
  `LASTMODIFIEDTIME` datetime NULL DEFAULT NULL COMMENT '最后操作时间',
  `CHANGEHISTORY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改历史',
  `TEMPHTML` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板正文',
  `PAGESIZE` decimal(5, 0) NULL DEFAULT NULL COMMENT '页码大小',
  `DATANUM` decimal(5, 0) NULL DEFAULT NULL COMMENT '要取出的数据量\r\n \r\n',
  `STARTPOSITION` decimal(5, 0) NULL DEFAULT NULL COMMENT '从何处取数据\r\n \r\n',
  `RIGHTCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '可视化右侧页面HTML代码',
  `THEMECSSNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题CSS文件名字',
  `THEMECSSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '主题CSS代码',
  `BASESTYLECSSNAME` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '风格组件CSS名字',
  `BASESTYLECSSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '风格组件CSS代码',
  `BASESTYLEJSNAME` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '风格组件JS名字',
  `BASESTYLEJSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '风格组件JS代码',
  `BASECONTACTCSSNAME` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交互组件CSS名字',
  `BASECONTACTCSSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交互组件CSS代码',
  `BASECONTACTJSNAME` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交互组件JS名字',
  `BASECONTACTJSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交互组件JS代码',
  `PORTALTYPE` decimal(5, 0) NULL DEFAULT NULL COMMENT '判断是否是可视化模板\r\n0或null 为不是  1为是',
  `STATUS` int(5) NULL DEFAULT NULL COMMENT '判断模板是可用还是处于回收站或者已被删除。\r\n0：回收站\r\n1：正常',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`TEMPID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '元数据模板' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tabletemplate
-- ----------------------------

-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template`  (
  `TEMPID` decimal(20, 0) NOT NULL COMMENT '主键pk',
  `SITEID` decimal(20, 0) NULL DEFAULT NULL COMMENT ' 站点ID',
  `TEMPNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ' 模板名称',
  `TEMPDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 模板描述',
  `TEMPEXT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '默认扩展名 例如.html\r\n \r\n',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 创建者\r\n \r\n',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间\r\n \r\n',
  `CRNUMBER` decimal(20, 0) NULL DEFAULT NULL COMMENT '创建人ID\r\n \r\n',
  `modify_by` decimal(20, 0) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `TEMPTYPE` decimal(5, 0) NULL DEFAULT NULL COMMENT '网页类型',
  `OUTPUTFILENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '输出文件名',
  `LASTMODIFIEDUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后操作用户',
  `LASTMODIFIEDTIME` datetime NULL DEFAULT NULL COMMENT '最后操作时间',
  `CHANGEHISTORY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改历史',
  `TEMPHTML` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板正文',
  `PAGESIZE` decimal(5, 0) NULL DEFAULT NULL COMMENT '页码大小',
  `DATANUM` decimal(5, 0) NULL DEFAULT NULL COMMENT '要取出的数据量\r\n \r\n',
  `STARTPOSITION` decimal(5, 0) NULL DEFAULT NULL COMMENT '从何处取数据\r\n \r\n',
  `RIGHTCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '可视化右侧页面HTML代码',
  `THEMECSSNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题CSS文件名字',
  `THEMECSSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '主题CSS代码',
  `BASESTYLECSSNAME` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '风格组件CSS名字',
  `BASESTYLECSSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '风格组件CSS代码',
  `BASESTYLEJSNAME` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '风格组件JS名字',
  `BASESTYLEJSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '风格组件JS代码',
  `BASECONTACTCSSNAME` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交互组件CSS名字',
  `BASECONTACTCSSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交互组件CSS代码',
  `BASECONTACTJSNAME` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交互组件JS名字',
  `BASECONTACTJSCONTENT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交互组件JS代码',
  `PORTALTYPE` decimal(5, 0) NULL DEFAULT NULL COMMENT '判断是否是可视化模板\r\n0或null 为不是  1为是',
  `STATUS` int(5) NULL DEFAULT NULL COMMENT '判断模板是可用还是处于回收站或者已被删除。\r\n0：回收站\r\n1：正常',
  `COLUMNID` decimal(20, 0) NULL DEFAULT NULL COMMENT '栏目ID',
  `THUMBNAIL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `TPLTYPE` int(2) NULL DEFAULT NULL COMMENT '模板类型',
  `VISUAL_TEMPLATE_ID` bigint(20) NULL DEFAULT NULL COMMENT '可视化产品ID',
  PRIMARY KEY (`TEMPID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of template
-- ----------------------------
INSERT INTO `template` VALUES (1, 1601100935430938625, '样例模板', NULL, 'html', 'admin', '2022-11-08 15:36:10', 1167324599977414658, NULL, NULL, 1, 'index', NULL, NULL, NULL, '<!Doctype html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0\"><title>首页</title><link rel=\"stylesheet\" href=\"/thirdparty/layui/css/layui.css\"><link rel=\"stylesheet\" href=\"/thirdparty/swiper/swiper.min.css\"><script src=\"/thirdparty/jquery/jquery-1.12.4.min.js\"></script><script type=\"text/javascript\" src=\"/thirdparty/swiper/swiper.min.js\"></script></head><body><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/419/layoutIndex.css\"><div class=\"firstEdit\"><div class=\"visual_row canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/376/top.css\"><div class=\"firstEdit\"><div class=\"visual_box\">\n    <div class=\"visual_top\">\n        <h1 class=\"visual_logo\"><a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/376/logo.jpg\"></a></h1>\n        <div class=\"visual_top_right\">\n            <form class=\"layui-form\">\n                <div class=\"layui-form-item\">\n                    <div class=\"layui-inline\" style=\"width:100px;\">\n                        <select lay-filter=\"\">\n                            <option value=\"1\">全部</option>\n                            <option value=\"2\">国家动态</option>\n                            <option value=\"3\">机床企业</option>\n                            <option value=\"4\">机床产品</option>\n                            <option value=\"5\">行业专家</option>\n                            <option value=\"6\">行业组织</option>\n                            <option value=\"7\">文献情报</option>\n                            <option value=\"8\">研究报告</option>\n                            <option value=\"9\">知识图谱</option>\n                        </select><div class=\"layui-unselect layui-form-select\"><div class=\"layui-select-title\"><input type=\"text\" placeholder=\"请选择\" value=\"全部\" readonly=\"\" class=\"layui-input layui-unselect\"><i class=\"layui-edge\"></i></div><dl class=\"layui-anim layui-anim-upbit\"><dd lay-value=\"1\" class=\"layui-this\">全部</dd><dd lay-value=\"2\" class=\"\">国家动态</dd><dd lay-value=\"3\" class=\"\">机床企业</dd><dd lay-value=\"4\" class=\"\">机床产品</dd><dd lay-value=\"5\" class=\"\">行业专家</dd><dd lay-value=\"6\" class=\"\">行业组织</dd><dd lay-value=\"7\" class=\"\">文献情报</dd><dd lay-value=\"8\" class=\"\">研究报告</dd><dd lay-value=\"9\" class=\"\">知识图谱</dd></dl></div>\n                    </div>\n                    <div class=\"layui-inline\">\n                        <input type=\"text\" name=\"name\" placeholder=\"请输入搜索内容\" autocomplete=\"off\" class=\"layui-input\">                      \n                    </div>\n                    <div class=\"layui-inline\">\n                        <button class=\"layui-btn\" lay-submit=\"\" lay-filter=\"\">\n                            <i class=\"layui-icon layui-icon-search layuiadmin-button-btn\"></i>\n                        </button>                        \n                    </div>\n                </div>\n            </form>\n        </div>\n    </div>\n</div></div></div>\n<div class=\"visual_main\">\n    <div class=\"visual_row canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/423/headerMenu.css\"><div class=\"firstEdit\"><div class=\"visual_menu\">\n    <ul>\n        <li class=\"ico1\"><a href=\"#\">首页</a></li>\n        <li class=\"ico2\"><a href=\"#\">行业动态</a></li>\n        <li class=\"ico3\"><a href=\"#\">机床企业</a></li>\n        <li class=\"ico4\"><a href=\"#\">机床产品</a></li>\n        <li class=\"ico5\"><a href=\"#\">行业专家</a></li>\n        <li class=\"ico6\"><a href=\"#\">行业组织</a></li>\n        <li class=\"ico7\"><a href=\"#\">文献情报</a></li>\n        <li class=\"ico8\"><a href=\"#\">研究报告</a></li>\n        <li class=\"ico9\"><a href=\"#\">知识图谱</a></li>\n    </ul>\n</div></div></div>\n    <div class=\"visual_row canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/410/menuStatistics.css\"><div class=\"firstEdit\"><div class=\"visual_menuStatistics\">    \n    <ul>\n        <li class=\"ico1\">机床企业<span>291</span>条</li>\n        <li class=\"ico2\">机床产品<span>30</span>条</li>\n        <li class=\"ico3\">行业专家<span>598</span>条</li>\n        <li class=\"ico4\">行业组织<span>1274</span>条</li>\n        <li class=\"ico5\">文献情报<span>198</span>条</li>\n        <li class=\"last ico6\">研究报告<span>1358</span>条</li>\n    </ul>\n</div></div></div>\n    <div class=\"visual_row\">\n        <div class=\"visual_left_a canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/422/carouselPic.css\"><div class=\"firstEdit\"><div class=\"visual_swiper_pic\">\n    <div class=\"swiper-container swiper-container-horizontal\">\n        <div class=\"swiper-wrapper\" style=\"transform: translate3d(-413px, 0px, 0px); transition-duration: 0ms;\"><div class=\"swiper-slide swiper-slide-duplicate swiper-slide-prev swiper-slide-duplicate-next\" data-swiper-slide-index=\"1\" style=\"width: 413px;\">\n              <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/422/01.png\"></a>\n              <h3><a href=\"#\">通用技术大连机床赴大船集团拜访交流</a></h3>\n              <div class=\"visual_bg\"></div>            \n            </div>\n            <div class=\"swiper-slide swiper-slide-active\" data-swiper-slide-index=\"0\" style=\"width: 413px;\">\n              <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/422/01.png\"></a>\n              <h3><a href=\"#\">通用技术大连机床赴大船集团拜访交流</a></h3>\n              <div class=\"visual_bg\"></div>            \n            </div>\n            <div class=\"swiper-slide swiper-slide-next swiper-slide-duplicate-prev\" data-swiper-slide-index=\"1\" style=\"width: 413px;\">\n              <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/422/01.png\"></a>\n              <h3><a href=\"#\">通用技术大连机床赴大船集团拜访交流</a></h3>\n              <div class=\"visual_bg\"></div>            \n            </div>\n        <div class=\"swiper-slide swiper-slide-duplicate swiper-slide-duplicate-active\" data-swiper-slide-index=\"0\" style=\"width: 413px;\">\n              <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/422/01.png\"></a>\n              <h3><a href=\"#\">通用技术大连机床赴大船集团拜访交流</a></h3>\n              <div class=\"visual_bg\"></div>            \n            </div></div>\n        <div class=\"swiper-pagination swiper-pagination-clickable swiper-pagination-bullets\"><span class=\"swiper-pagination-bullet\"></span><span class=\"swiper-pagination-bullet\"></span><span class=\"swiper-pagination-bullet swiper-pagination-bullet-active\"></span><span class=\"swiper-pagination-bullet\"></span><span class=\"swiper-pagination-bullet\"></span><span class=\"swiper-pagination-bullet\"></span><span class=\"swiper-pagination-bullet\"></span><span class=\"swiper-pagination-bullet\"></span></div>\n        <div class=\"swiper-button-next\"></div>\n        <div class=\"swiper-button-prev\"></div>\n    </div>\n</div></div><script src=\"http://36.138.169.165:8081/files/visual/js/422/carouselPic.js\"></script></div>\n        <div class=\"visual_left_b canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/415/textList.css\"><div class=\"firstEdit\"><div class=\"visual_list_text\">\n    <div class=\"visual_title\"><div class=\"more\"><a href=\"#\">更多 &gt;</a></div><h3>机床要闻</h3></div>\n    <div class=\"visual_con\">\n        <ul>\n            <li><span>2021-07-19</span><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></li>\n            <li><span>2021-07-19</span><a href=\"#\">我院开展安全生产大检查和应急演练活动</a></li>\n            <li><span>2021-07-19</span><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></li>\n            <li><span>2021-07-19</span><a href=\"#\">我院开展安全生产大检查和应急演练活动</a></li>\n            <li><span>2021-07-19</span><a href=\"#\">我院开展安全生产大检查和应急演练活动</a></li>\n        </ul>\n    </div>\n</div></div></div>\n        <div class=\"visual_left_c canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/416/textList2.css\"><div class=\"firstEdit\"><div class=\"visual_list_text2\">\n    <div class=\"visual_title\"><div class=\"more\"><a href=\"#\">更多 &gt;</a></div><h3>最新研报</h3></div>\n    <div class=\"visual_con\">\n        <ul>\n            <li><a href=\"#\">公司简评报告：业绩符合预期</a></li>\n            <li><a href=\"#\">国内高端数控机床龙头</a></li>\n            <li><a href=\"#\">公司简评报告：业绩符合预期</a></li>\n            <li><a href=\"#\">国内高端数控机床龙头</a></li>\n            <li><a href=\"#\">公司简评报告：业绩符合预期</a></li>\n        </ul>\n    </div>\n</div></div></div>\n    </div>\n    <div class=\"visual_row\">\n        <div class=\"visual_left_a canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/417/textList3.css\"><div class=\"firstEdit\"><div class=\"visual_list_text3\">\n    <div class=\"visual_title\"><div class=\"more\"><a href=\"#\">更多 &gt;</a></div><h3>企业</h3><h4><span>推荐</span><span class=\"en\">Enterprise recommendation</span></h4></div>\n    <div class=\"visual_con\">\n        <ul>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n        </ul>\n    </div>\n</div></div></div>\n        <div class=\"visual_left_b canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/417/textList3.css\"><div class=\"firstEdit\"><div class=\"visual_list_text3\">\n    <div class=\"visual_title\"><div class=\"more\"><a href=\"#\">更多 &gt;</a></div><h3>企业</h3><h4><span>推荐</span><span class=\"en\">Enterprise recommendation</span></h4></div>\n    <div class=\"visual_con\">\n        <ul>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n            <li>\n                <h4><a href=\"#\">我院举办黄文熙讲座（第25讲）学术报告会</a></h4>\n                <span>企业标签：上市公司</span>\n            </li>\n        </ul>\n    </div>\n</div></div></div>\n        <div class=\"visual_left_c canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/421/textList2.css\"><div class=\"firstEdit\"><div class=\"visual_list_text2\">\n    <div class=\"visual_title\"><div class=\"more\"><a href=\"#\">更多 &gt;</a></div><h3>最新研报</h3></div>\n    <div class=\"visual_con\">\n        <ul>\n            <li><a href=\"#\">公司简评报告：业绩符合预期</a></li>\n            <li><a href=\"#\">国内高端数控机床龙头</a></li>\n            <li><a href=\"#\">公司简评报告：业绩符合预期</a></li>\n            <li><a href=\"#\">国内高端数控机床龙头</a></li>\n            <li><a href=\"#\">公司简评报告：业绩符合预期</a></li>\n            <li><a href=\"#\">国内高端数控机床龙头</a></li>\n            <li><a href=\"#\">公司简评报告：业绩符合预期</a></li>\n            <li><a href=\"#\">国内高端数控机床龙头</a></li>\n            <li><a href=\"#\">公司简评报告：业绩符合预期</a></li>\n            <li><a href=\"#\">国内高端数控机床龙头</a></li>\n            <li><a href=\"#\">公司简评报告：业绩符合预期</a></li>\n        </ul>\n    </div>\n</div></div></div>\n    </div>\n    <div class=\"visual_row canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/418/carouselPic2.css\"><div class=\"firstEdit\"><div class=\"visual_swiper_pic2\">\n    <div class=\"visual_title\"><div class=\"more\"><a href=\"#\">更多 &gt;</a></div><h3>专家</h3><h4><span>智库</span><span class=\"en\">Expert think tank</span></h4></div>\n    <div class=\"visual_swiper_container\">\n      <div class=\"swiper-button-prev\"></div>\n      <div class=\"swiper-container swiper-container-horizontal\">\n        <div class=\"swiper-wrapper\" style=\"transform: translate3d(-1566px, 0px, 0px); transition-duration: 0ms;\"><div class=\"swiper-slide swiper-slide-duplicate swiper-slide-duplicate-prev\" data-swiper-slide-index=\"1\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate swiper-slide-duplicate-active\" data-swiper-slide-index=\"2\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate swiper-slide-duplicate-next\" data-swiper-slide-index=\"3\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate\" data-swiper-slide-index=\"4\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate\" data-swiper-slide-index=\"5\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate\" data-swiper-slide-index=\"6\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate\" data-swiper-slide-index=\"7\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div>\n          <div class=\"swiper-slide\" data-swiper-slide-index=\"0\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div>\n          <div class=\"swiper-slide swiper-slide-prev\" data-swiper-slide-index=\"1\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div>\n          <div class=\"swiper-slide swiper-slide-active\" data-swiper-slide-index=\"2\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div>\n          <div class=\"swiper-slide swiper-slide-next\" data-swiper-slide-index=\"3\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div>\n          <div class=\"swiper-slide\" data-swiper-slide-index=\"4\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div>\n          <div class=\"swiper-slide\" data-swiper-slide-index=\"5\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div>\n          <div class=\"swiper-slide\" data-swiper-slide-index=\"6\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div>\n          <div class=\"swiper-slide\" data-swiper-slide-index=\"7\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div>\n        <div class=\"swiper-slide swiper-slide-duplicate\" data-swiper-slide-index=\"0\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate swiper-slide-duplicate-prev\" data-swiper-slide-index=\"1\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate swiper-slide-duplicate-active\" data-swiper-slide-index=\"2\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate swiper-slide-duplicate-next\" data-swiper-slide-index=\"3\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate\" data-swiper-slide-index=\"4\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate\" data-swiper-slide-index=\"5\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div><div class=\"swiper-slide swiper-slide-duplicate\" data-swiper-slide-index=\"6\" style=\"width: 154px; margin-right: 20px;\">\n            <a href=\"#\"><img src=\"http://36.138.169.165:8081/files/visual/img/418/02.png\"></a>\n            <h3><a href=\"#\">孙序泉</a></h3>\n            <p>级别：杰出青年</p>\n          </div></div>\n      </div>\n      <div class=\"swiper-button-next\"></div>\n    </div>\n  </div></div><script src=\"http://36.138.169.165:8081/files/visual/js/418/232.js\"></script></div>\n</div>\n<div class=\"visual_row canEdit\"><link rel=\"stylesheet\" href=\"http://36.138.169.165:8081/files/visual/css/386/217.css\"><div class=\"firstEdit\"><div class=\"visual_footer\">\n    <p>\n        <a href=\"http://www.amtonline.org/\" target=\"_blank\">美国机械制造技术协会</a> |\n        <a href=\"http://www.ucimu.it/\" target=\"_blank\">意大利机床协会</a> |\n        <a href=\"http://www.afm.es/\" target=\"_blank\">西班牙机床协会</a> |\n        <a href=\"http://www.vdw.de/\" target=\"_blank\">德国机械制造商协会</a> |\n        <a href=\"http://www.swissmem.ch/\" target=\"_blank\">瑞士制造协会</a>\n        <a href=\"http://www.nikkohan.or.jp/\" target=\"_blank\">日本机床协会</a> |\n        <a href=\"http://www.komma.org/\" target=\"_blank\">韩国机床协会</a> |\n        <a href=\"http://www.tami.org.tw/\" target=\"_blank\">台湾机械同业会</a> |\n        <a href=\"http://www.cmtba.org.cn/\" target=\"_blank\">中国机床工具协会</a>\n    </p>\n    <p>版权所有通用技术集团机床工程研究院</p>\n</div></div></div></div></body><script src=\"/thirdparty/layui/layui.all.js\"></script><script type=\"text/javascript\" src=\"/common/js/config.js\"></script><script type=\"text/javascript\" src=\"/common/js/common.js\"></script><script type=\"text/javascript\" src=\"/files/visual/template/js/index.js\"></script></html>', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo`  (
  `USERID` bigint(20) NOT NULL COMMENT '主键id',
  `USERNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `USERAGE` int(20) NULL DEFAULT NULL,
  `NICKNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `TRUENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `MDPASSWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密密码',
  `PASSWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实密码',
  `GROUPID` bigint(20) NULL DEFAULT NULL COMMENT '组织id',
  `CREDIT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户积分',
  `REGTIME` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `ADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户地址',
  `TEL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `MOBILE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `EMAIL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `LOGINTIME` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `LOGOUTTIME` datetime NULL DEFAULT NULL COMMENT '登出时间',
  `STATUS` int(10) NULL DEFAULT NULL COMMENT '用户状态',
  `LASTLOGINTIME` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `LASTLOGINIP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  `CARDID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `DUTIES` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务',
  `SEX` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `NATION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '民族',
  `ISACTOR` int(10) NULL DEFAULT NULL COMMENT '参会类型',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  `headUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像url',
  `DESCRIBE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `SIGN` int(10) NULL DEFAULT NULL COMMENT '0：系统用户，3：互联网用户',
  `TIMES` bigint(255) NULL DEFAULT 0 COMMENT '登录次数',
  `IDCARDA` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证正面路径',
  `IDCARDB` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证背面路径',
  `BIRTHDATE` datetime NULL DEFAULT NULL COMMENT '出生日期',
  `MARRYSTATE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '婚姻状况',
  `WEINXIN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信',
  `QQNUMBER` bigint(10) NULL DEFAULT NULL COMMENT 'qq号',
  `CONTACTS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用联系人',
  `RESERVEPHONE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用联系方式',
  `DIPLOMAA` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '毕业证',
  `DIPLOMAB` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学位证',
  `OTHERCARD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其他证件',
  `NATIVEPLACE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `BIRTHADRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '户籍地址',
  `NORMALADRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '常住地址',
  `ISENTRY` int(255) NULL DEFAULT NULL COMMENT '入离职状态',
  `LEADERID` bigint(20) NULL DEFAULT NULL COMMENT '直属领导',
  `POWERSTATE` int(2) NULL DEFAULT 0 COMMENT '权限状态:永久用户：空/0，临时权限：1，权限到期2',
  `POWERDATE` datetime NULL DEFAULT NULL COMMENT '到期时间',
  `STOPSTATE` int(2) NULL DEFAULT 0 COMMENT '权限状态:永久用户：空/0，临时权限：1，权限到期2',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `SIGNATURE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `HOBBY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `WORKPLACE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `job` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务',
  `OPENID_WX` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'openid for 微信',
  `OPENID_WB` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'openid for 微博',
  `OPENID_QQ` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'openid for qq',
  `WBNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微博昵称',
  `WXNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信昵称',
  `QQNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'qq昵称',
  `BUSINESS_ID` bigint(20) NULL DEFAULT NULL COMMENT '企业id',
  `INVITATION_CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `LOCKTIMES` int(11) NULL DEFAULT NULL COMMENT '错误次数',
  `SALER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销售',
  `OPENID_APPLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'openid for apple',
  `APPLENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'apple昵称',
  PRIMARY KEY (`USERID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES (1, 'admin', 0, '李小明', 'admin', '0192023a7bbd73250516f069df18b500', 'admin123', NULL, NULL, '2019-11-21 14:53:28', '111.201.130.192', '18625128855', '18911122442', 'admin123@qq.com', '2022-12-09 15:34:27', '2022-12-09 15:01:47', 0, '2019-11-27 14:39:24', '192.168.0.104', '110828525555555', NULL, '女', '汉', 0, 'admin', NULL, '2020-04-27 09:57:51', 1, '2022-12-09 15:34:27', '/files/headimg/6a66a4fd-7b15-4cb3-9526-98c1acf32fda.jpg', '1331425', 0, 1, '/files/1574473441008.png', '/files/1574473446892.jpg', '1991-03-13 00:00:00', '未婚', '123456789', 123456987, '威尔', '123456789', '/files/1574415338480.jpg', '/files/1574415338548.jpg', '/files/1574419200589.jpg,/files/1574419212844.jpg,/files/1574419568823.jpg,/files/1574419815461.png', '北京海淀区', '加利福尼亚', 'qqww', 100, NULL, 0, NULL, 0, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO `userinfo` VALUES (10, 'sjgly', NULL, NULL, '审计管理员', '9f607dd8576b6dbcfc92f1776bb4e67c', 'sjgly123', NULL, NULL, '2022-12-09 14:18:35', NULL, NULL, '', 'sjgly@123.com', '2022-12-09 14:24:01', '2022-12-09 14:24:03', 0, NULL, '192.168.0.104', NULL, NULL, NULL, NULL, 0, 'admin', 1, '2022-12-09 14:18:35', 10, '2022-12-09 14:24:04', NULL, NULL, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, 0, NULL, 0, 'sjgly', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL);
INSERT INTO `userinfo` VALUES (1601098737791803394, 'ituser', NULL, NULL, '样例互联网用户', '4297f44b13955235245b2497399d7a93', '123123', NULL, NULL, '2022-12-09 14:17:41', NULL, NULL, '18111111111', '123@123.com', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'admin', 1, '2022-12-09 14:17:41', NULL, NULL, '', NULL, 3, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for visual_component
-- ----------------------------
DROP TABLE IF EXISTS `visual_component`;
CREATE TABLE `visual_component`  (
  `component_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `component_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名称',
  `component_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件类型：0-自定义，1-基础组件，2-应用组件，3-报表组件',
  `detail_table` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件具体信息表',
  `order_number` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`component_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of visual_component
-- ----------------------------
INSERT INTO `visual_component` VALUES (1, '登录框', '2', 'visual_component_login', 6);
INSERT INTO `visual_component` VALUES (2, '文本', '1', 'visual_component_text', 1);
INSERT INTO `visual_component` VALUES (3, '图片', '1', 'visual_component_picture', 2);
INSERT INTO `visual_component` VALUES (4, '视频', '1', 'visual_component_video', 6);
INSERT INTO `visual_component` VALUES (5, '搜索', '2', 'visual_component_search', 5);

-- ----------------------------
-- Table structure for visual_module
-- ----------------------------
DROP TABLE IF EXISTS `visual_module`;
CREATE TABLE `visual_module`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `CSS_CODE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `EXTE_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `HTML_CODE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `IMG` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `IND_CLASS` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JS_CODE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `NET_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OUT_FILE_NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PRO_DES` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `SCREEN_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TITLE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PID` bigint(20) NOT NULL DEFAULT 0,
  `STATUS` int(2) NULL DEFAULT 0,
  `MODULE_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TYPE` int(2) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `VM_TYPE` bigint(20) NULL DEFAULT NULL,
  `VC_TYPE` bigint(20) NULL DEFAULT NULL,
  `FRAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `HTML_CODEN` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `HTML_CODENUSE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 487 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of visual_module
-- ----------------------------

-- ----------------------------
-- Table structure for visual_template
-- ----------------------------
DROP TABLE IF EXISTS `visual_template`;
CREATE TABLE `visual_template`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CRTIME` datetime NULL DEFAULT NULL,
  `CRNUMBER` bigint(18) NULL DEFAULT NULL,
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `CSS_CODE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `EXTE_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `HTML_CODE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `IMG` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `IND_CLASS` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JS_CODE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `NET_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `OUT_FILE_NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PRO_DES` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `SCREEN_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TITLE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STATUS` int(2) NULL DEFAULT 0,
  `TYPE` int(2) NULL DEFAULT NULL,
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TEMPLATE_TYPE` bigint(20) NULL DEFAULT NULL,
  `MJ_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BUSINESS_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 249 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of visual_template
-- ----------------------------

-- ----------------------------
-- Table structure for watermarkinfo
-- ----------------------------
DROP TABLE IF EXISTS `watermarkinfo`;
CREATE TABLE `watermarkinfo`  (
  `ID` bigint(20) NOT NULL COMMENT '主键id',
  `CSIZE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '压缩尺寸',
  `INFOSIGN` int(10) NULL DEFAULT NULL COMMENT '是否自动抽取图片信息',
  `SUPFORM` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支持格式',
  `UPSIZE` double(255, 0) NULL DEFAULT NULL COMMENT '上传大小',
  `COPYRIGHT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属版权',
  `SUMMARYSIGN` int(10) NULL DEFAULT NULL COMMENT '是否汇总子栏目',
  `WATERMARKSIGN` int(10) NULL DEFAULT NULL COMMENT '是否添加水印',
  `CONTENT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文字内容',
  `RED` int(20) NULL DEFAULT NULL COMMENT '红',
  `GREEN` int(20) NULL DEFAULT NULL COMMENT '绿',
  `BLUE` int(20) NULL DEFAULT NULL COMMENT '蓝',
  `ALPHA` int(20) NULL DEFAULT NULL COMMENT '透明度',
  `LOCATION` int(20) NULL DEFAULT NULL COMMENT '位置',
  `FONTSIZE` int(20) NULL DEFAULT NULL COMMENT '字体大小',
  `INPUTFONT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字体',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `WMKPATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片水印路径',
  `PICALPHA` int(11) NULL DEFAULT NULL COMMENT '图片水印透明度',
  `WMKSIGN` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0文字水印，1图片水印',
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '水印名',
  `ISCOMPRESS` int(10) NULL DEFAULT NULL COMMENT '1压缩设置，2版权设置，',
  `COMPRESSRESO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '压缩比例',
  `CPSW` int(20) NULL DEFAULT NULL COMMENT '压缩尺寸（宽）',
  `CPSH` int(20) NULL DEFAULT NULL COMMENT '压缩尺寸（高）',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of watermarkinfo
-- ----------------------------

-- ----------------------------
-- Table structure for websiteinfo
-- ----------------------------
DROP TABLE IF EXISTS `websiteinfo`;
CREATE TABLE `websiteinfo`  (
  `SITEID` bigint(20) NOT NULL COMMENT '主键id',
  `COMPANYID` bigint(20) NULL DEFAULT NULL COMMENT '所属企业id',
  `SITENAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站点名称',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一标识',
  `SITEDESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `DATAPATH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站点发布的目录',
  `WEBURL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站点url',
  `STATUS` int(5) NOT NULL DEFAULT 0 COMMENT '创建状态',
  `SITEORDER` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `HOMETEMPLATEID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '首页模板id',
  `DETAILTEMPLATEID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '细览模板id',
  `LASTMODIFYTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后操作时间',
  `SEOINFO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'seo描述',
  `SEOTITLE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'seo标题',
  `SEOKEYWORDS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'seo关键字',
  `PLATFORM` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布平台',
  `YPUBLISH` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '适合发布的类型',
  `CPYNAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业信息表公司名称',
  `CRUSER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `CRNUMBER` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `CRTIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFY_BY` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人id',
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `WEBCLASS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站点类型',
  `SITESTATUS` int(255) NULL DEFAULT 0 COMMENT '停用站点，1为停用，0为正常',
  `MODIFY_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人名称',
  `MODULE_ID` bigint(20) NULL DEFAULT NULL COMMENT '模块id',
  `MODULE_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  PRIMARY KEY (`SITEID`, `STATUS`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '站点信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of websiteinfo
-- ----------------------------
INSERT INTO `websiteinfo` VALUES (1549310008678813698, 1549309528875601922, '可视化风格管理', 'visualStyle', NULL, 'visualStyle', 'http://36.138.169.165:8081/pub/', 0, 0, '', '', '2022-12-09 06:33:50', NULL, NULL, NULL, NULL, NULL, NULL, 'admin', 1, '2022-12-09 06:33:50', 1, '2022-12-09 06:33:50', '1', 0, 'admin', NULL, '无');
INSERT INTO `websiteinfo` VALUES (1601100935430938625, 1167323268476895233, '样例元数据站点', 'ylzd', NULL, 'ylzd', '1', 0, 1, '', '', '2022-12-09 06:28:18', NULL, NULL, NULL, NULL, NULL, NULL, 'admin', 1, '2022-12-09 06:28:18', 1, '2022-12-09 14:28:54', '1', 0, 'admin', NULL, '无');
INSERT INTO `websiteinfo` VALUES (1601101087986163714, 1167323268476895233, '样例图片站点', 'yltpzd', NULL, 'yltpzd', '1', 0, 3, '', '', '2022-12-09 06:28:19', NULL, NULL, NULL, NULL, NULL, NULL, 'admin', 1, '2022-12-09 06:28:19', 1, '2022-12-09 14:28:54', '3', 0, 'admin', NULL, '无');
INSERT INTO `websiteinfo` VALUES (1601101189513486337, 1167323268476895233, '样例视频站点', 'ylspzd', NULL, 'ylspzd', '1', 0, 0, '', '', '2022-12-09 06:28:18', NULL, NULL, NULL, NULL, NULL, NULL, 'admin', 1, '2022-12-09 06:28:18', 1, '2022-12-09 14:28:54', '6', 0, 'admin', NULL, '无');
INSERT INTO `websiteinfo` VALUES (1601101295927173121, 1167323268476895233, '样例文件站点', 'ylwjzd', NULL, 'ylwjzd', '1', 0, 6, '', '', '2022-12-09 06:28:19', NULL, NULL, NULL, NULL, NULL, NULL, 'admin', 1, '2022-12-09 06:28:19', 1, '2022-12-09 14:28:54', '4', 0, 'admin', NULL, '无');
INSERT INTO `websiteinfo` VALUES (1601101376998875137, 1167323268476895233, '样例音频站点', 'ylypzd', NULL, 'ylypzd', '1', 0, 8, '', '', '2022-12-09 06:28:19', NULL, NULL, NULL, NULL, NULL, NULL, 'admin', 1, '2022-12-09 06:28:19', 1, '2022-12-09 14:28:54', '5', 0, 'admin', NULL, '无');

-- ----------------------------
-- View structure for act_id_group
-- ----------------------------
DROP VIEW IF EXISTS `act_id_group`;
CREATE VIEW `act_id_group` AS (select `roleinfo`.`ROLEID` AS `ID_`,'1' AS `REV_`,`roleinfo`.`ROLENAME` AS `NAME_`,'role' AS `TYPE_` from `roleinfo`);

-- ----------------------------
-- View structure for act_id_membership
-- ----------------------------
DROP VIEW IF EXISTS `act_id_membership`;
CREATE VIEW `act_id_membership` AS (select `role_user`.`USERID` AS `USER_ID_`,`role_user`.`ROLEID` AS `GROUP_ID_` from `role_user`);

-- ----------------------------
-- View structure for act_id_user
-- ----------------------------
DROP VIEW IF EXISTS `act_id_user`;
CREATE VIEW `act_id_user` AS (select `userinfo`.`USERID` AS `ID_`,'1' AS `REV_`,`userinfo`.`USERNAME` AS `FIRST_`,NULL AS `LAST_`,`userinfo`.`TRUENAME` AS `DISPLAY_NAME_`,NULL AS `EMAIL_`,NULL AS `PWD_`,NULL AS `PICTURE_ID_`,NULL AS `TENANT_ID_` from `userinfo`);

SET FOREIGN_KEY_CHECKS = 1;

CREATE SCHEMA MYDOMAINID;

CREATE SEQUENCE MYDOMAINID.SEQ_PARENT START WITH 20001;
CREATE TABLE MYDOMAINID.PARENT
(
    ID               NUMBER(9, 0) NOT NULL,
    NOME             VARCHAR2(100),
    SIBLING_ID       NUMBER(9, 0) NOT NULL,
    CONSTRAINT PK_PARENT PRIMARY KEY (ID)
);

CREATE SEQUENCE MYDOMAINID.SEQ_CHILD START WITH 50001;
CREATE TABLE MYDOMAINID.CHILD
(
    KEY                 NUMBER(9, 0) NOT NULL,
    PARENT_ID           NUMBER(9, 0) NOT NULL,
    NAME                VARCHAR2(100),
    ANOTHER_CHILD_KEY   NUMBER(9, 0),
    CONSTRAINT PK_CHILD PRIMARY KEY (KEY)
);

CREATE SEQUENCE MYDOMAINID.SEQ_GRANDCHILD START WITH 60001;
CREATE TABLE MYDOMAINID.GRANDCHILD
(
    ID                  NUMBER(9, 0) NOT NULL,
    PARENT_CHILD_KEY    NUMBER(9, 0) NOT NULL,
    NAME                VARCHAR2(100),
    CONSTRAINT PK_GRANDCHILD PRIMARY KEY (ID)
);
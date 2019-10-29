package pers.fanjc.enums;

import org.apache.poi.ss.usermodel.DateUtil;

public enum FieldTypeStrategy {
    STRING {
        @Override
        public Object transform(String value) {
            return value;
        }
    },
    INTEGER {
        @Override
        public Object transform(String value) {
            return "".equals(value) ? null : Integer.parseInt(value);
        }
    },
    LONG {
        @Override
        public Object transform(String value) {
            return "".equals(value) ? null : Long.parseLong(value);
        }
    },
    DOUBLE {
        @Override
        public Object transform(String value) {
            return "".equals(value) ? null : Double.parseDouble(value);
        }
    },
    DATE {
        @Override
        public Object transform(String value) {
            return "".equals(value) ? null : DateUtil.getJavaDate(Double.parseDouble(value));
        }
    };

    public abstract Object transform(String value);
}

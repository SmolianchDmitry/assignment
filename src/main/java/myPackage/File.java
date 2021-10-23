package myPackage;

import java.util.List;

public class File {

        private String file;
        private boolean isFile = false;
        private List<String> directory;

        public String getFile() {
                return file;
        }

        public void setFile(String file) {
                this.file = file;
        }

        public boolean isFile() {
                return isFile;
        }

        public void setFile(boolean file) {
                isFile = file;
        }

        public List<String> getDirectory() {
                return directory;
        }

        public void setDirectory(List<String> directory) {
                this.directory = directory;
        }
}

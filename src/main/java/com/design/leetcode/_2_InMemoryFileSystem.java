package com.design.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Design a data structure that simulates an in-memory file system.

Implement the FileSystem class:

FileSystem() Initializes the object of the system.
List<String> ls(String path)
If path is a file path, returns a list that only contains this file's name.
If path is a directory path, returns the list of file and directory names in this directory.
The answer should in lexicographic order.
void mkdir(String path) Makes a new directory according to the given path. The given directory path does not exist. If the middle directories in the path do not exist, 
you should create them as well.
void addContentToFile(String filePath, String content)
If filePath does not exist, creates that file containing given content.
If filePath already exists, appends the given content to original content.
String readContentFromFile(String filePath) Returns the content in the file at filePath.
 

Example 1:


Input
["FileSystem", "ls", "mkdir", "addContentToFile", "ls", "readContentFromFile"]
[[], ["/"], ["/a/b/c"], ["/a/b/c/d", "hello"], ["/"], ["/a/b/c/d"]]
Output
[null, [], null, null, ["a"], "hello"]

Explanation
FileSystem fileSystem = new FileSystem();
fileSystem.ls("/");                         // return []
fileSystem.mkdir("/a/b/c");
fileSystem.addContentToFile("/a/b/c/d", "hello");
fileSystem.ls("/");                         // return ["a"]
fileSystem.readContentFromFile("/a/b/c/d"); // return "hello"
 

Constraints:

1 <= path.length, filePath.length <= 100
path and filePath are absolute paths which begin with '/' and do not end with '/' except that the path is just "/".
You can assume that all directory names and file names only contain lowercase letters, and the same names will not exist in the same directory.
You can assume that all operations will be passed valid parameters, and users will not attempt to retrieve file content or list a directory or file that does not exist.
1 <= content.length <= 50
At most 300 calls will be made to ls, mkdir, addContentToFile, and readContentFromFile.
 * 
 * @author sukh
 *
 */
public class _2_InMemoryFileSystem {

  private class Dir {
    private Map<String, String> files;
    private Map<String, Dir> dirs;

    private Dir() {
      files = new HashMap<>();
      dirs = new HashMap<>();
    }
  }

  private Dir root;

  public _2_InMemoryFileSystem() {
    root = new Dir();
  }

  /**
   * Time: O(m + n + k log k) <br>
   * m refers to the length of the input string. We need to scan the input string
   * once to split it and determine the various levels. n refers to the depth of
   * the last directory level in the given input for ls. This factor is taken
   * because we need to enter n levels of the tree structure to reach the last
   * level. k refers to the number of entries(files+subdirectories) in the last
   * level directory(in the current input). We need to sort these names giving a
   * factor of klog(k).
   * 
   * @param path
   * @return
   */
  public List<String> ls(String path) {
    List<String> files = new ArrayList<>();
    Dir dir = root;
    if (!path.equals("/")) {
      String[] paths = path.split("/");
      for (int i = 1; i < paths.length - 1; i++) {
        dir = dir.dirs.get(paths[i]);
      }
      if (!dir.dirs.containsKey(paths[paths.length - 1])) {
        files.add(paths[paths.length - 1]);
        return files;
      }
      dir = dir.dirs.get(paths[paths.length - 1]);
    }
    files.addAll(new ArrayList<>(dir.dirs.keySet()));
    files.addAll(new ArrayList<>(dir.files.keySet()));
    Collections.sort(files);
    return files;
  }

  /**
   * Time: O(m + n) <br>
   * m refers to the length of the input string. We need to scan the input string
   * once to split it and determine the various levels. n refers to the depth of
   * the last directory level in the mkdir input. This factor is taken because we
   * need to enter n levels of the tree structure to reach the last level.
   * 
   * @param path
   */
  public void mkdir(String path) {
    Dir dir = root;
    String[] paths = path.split("/");
    for (int i = 1; i < paths.length; i++) {
      if (!dir.dirs.containsKey(paths[i])) {
        dir.dirs.put(paths[i], new Dir());
      }
      dir = dir.dirs.get(paths[i]);
    }
  }

  /**
   * Time: O(m + n) <br>
   * m refers to the length of the input string. We need to scan the input string
   * once to split it and determine the various levels. n refers to the depth of
   * the last directory level in the mkdir input. This factor is taken because we
   * need to enter n levels of the tree structure to reach the last level.
   * 
   * @param filePath
   * @param content
   */
  public void addContentToFile(String filePath, String content) {
    Dir dir = root;
    String[] paths = filePath.split("/");
    for (int i = 1; i < paths.length - 1; i++) {
      if (!dir.dirs.containsKey(paths[i])) {
        dir.dirs.put(paths[i], new Dir());
      }
      dir = dir.dirs.get(paths[i]);
    }
    String file = dir.files.getOrDefault(paths[paths.length - 1], "");
    dir.files.put(paths[paths.length - 1], file.concat(content));
  }

  /**
   * Time: O(m + n) <br>
   * m refers to the length of the input string. We need to scan the input string
   * once to split it and determine the various levels. n refers to the depth of
   * the last directory level in the mkdir input. This factor is taken because we
   * need to enter n levels of the tree structure to reach the last level.
   * 
   * @param filePath
   * @return
   */
  public String readContentFromFile(String filePath) {
    Dir dir = root;
    String[] paths = filePath.split("/");
    for (int i = 1; i < paths.length - 1; i++) {
      dir = dir.dirs.get(paths[i]);
    }
    return dir.files.get(paths[paths.length - 1]);
  }

}

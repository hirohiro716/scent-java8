package com.hirohiro716.robot;

import java.util.ArrayList;
import java.util.Collections;

import com.hirohiro716.ArrayHelper;
import com.hirohiro716.StringConverter;

/**
 * 自動のキー入力機能の抽象クラス.
 * @author hiro
 * @param <T> KeyCodeの型
 */
public abstract class AbstractTypingTask<T> implements InterfaceTypingTask<T> {
    
    private ArrayList<Task> tasks = new ArrayList<>();
    
    @Override
    public int getNumberOfTasks() {
        return this.tasks.size();
    }
    
    @Override
    public String makeTaskDefinitionString() {
        StringBuilder result = new StringBuilder();
        for (Task task: this.tasks) {
            if (result.length() > 0) {
                result.append(DEFINITION_STRING_TASK_DELIMITER);
            }
            switch (task.getTaskType()) {
            case KEY:
                result.append(task.getTaskType().toString());
                result.append(DEFINITION_STRING_TYPE_AND_VALUE_DELIMITER);
                ArrayList<KeyCode> keyCodes = new ArrayList<>();
                for (T keyCode: task.getKeyCodes()) {
                    keyCodes.add(this.findTaskKeyCode(keyCode));
                }
                result.append(ArrayHelper.join(keyCodes, DEFINITION_STRING_VALUES_DELIMITER));
                break;
            case SLEEP:
                result.append(task.getTaskType().toString());
                result.append(DEFINITION_STRING_TYPE_AND_VALUE_DELIMITER);
                result.append(task.getMilliseconds());
                break;
            }
        }
        return result.toString();
    }
    
    @Override
    public void importFromTaskDefinitionString(String taskDefinitionString) {
        this.tasks.clear();
        try {
            String[] taskStrings = StringConverter.nullReplace(taskDefinitionString, "").split(DEFINITION_STRING_TASK_DELIMITER);
            for (String taskString: taskStrings) {
                String[] typeAndValue = taskString.split(DEFINITION_STRING_TYPE_AND_VALUE_DELIMITER);
                TaskType taskType = TaskType.find(typeAndValue[0]);
                switch (taskType) {
                case KEY:
                    String[] keyCodeStrings = typeAndValue[1].split(DEFINITION_STRING_VALUES_DELIMITER);
                    ArrayList<KeyCode> keyCodes = new ArrayList<>();
                    for (String keyCodeString: keyCodeStrings) {
                        KeyCode keyCode = KeyCode.find(keyCodeString);
                        keyCodes.add(keyCode);
                    }
                    this.addKeyTypeTask(keyCodes.toArray(new KeyCode[] {}));
                    break;
                case SLEEP:
                    long milliseconds = StringConverter.stringToLong(typeAndValue[1]);
                    this.addSleepTask(milliseconds);
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    @Override
    public void addKeyTypeTask(KeyCode... keyCodes) {
        ArrayList<T> typingKeyCodes = new ArrayList<>();
        for (KeyCode keyCode: keyCodes) {
            typingKeyCodes.add(this.findTypingKeyCode(keyCode));
        }
        this.tasks.add(new Task(typingKeyCodes));
    }

    @Override
    public void addSleepTask(long milliseconds) {
        this.tasks.add(new Task(milliseconds));
    }
    
    @Override
    public void execute() {
        for (Task task: this.tasks) {
            switch (task.getTaskType()) {
            case KEY:
                this.keyType(task.getKeyCodes());
                break;
            case SLEEP:
                try {
                    Thread.sleep(task.getMilliseconds());
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * タスクの種類列挙型.
     * @author hiro
     */
    private enum TaskType {
        /**
         * キー入力
         */
        KEY,
        /**
         * 待機
         */
        SLEEP,
        ;
        
        /**
         * 文字列からTaskTypeの取得を試みる.
         * @param string 文字列 
         * @return TaskType
         */
        public static TaskType find(String string) {
            for (TaskType taskType: TaskType.values()) {
                if (string != null) {
                    if (taskType.toString().toLowerCase().equals(string.toLowerCase()) || taskType.toString().toUpperCase().equals(string.toUpperCase())) {
                        return taskType;
                    }
                }
            }
            return null;
        }
        
    }
    
    /**
     * 自動キー入力のタスクのクラス.
     * @author hiro
     */
    private class Task {

        /**
         * キー入力するTaskを作成する.
         * @param keyCodes
         */
        public Task(ArrayList<T> keyCodes) {
            this.taskType = TaskType.KEY;
            this.keyCodes = new ArrayList<>();
            this.keyCodes.addAll(keyCodes);
            Collections.unmodifiableList(this.keyCodes);
        }
        
        /**
         * 待機するTaskを作成する.
         * @param milliseconds ミリ秒
         */
        public Task(long milliseconds) {
            this.taskType = TaskType.SLEEP;
            this.milliseconds = milliseconds;
        }
        
        private TaskType taskType;
        
        /**
         * タスクの種類を取得する.
         * @return タスクの種類
         */
        public TaskType getTaskType() {
            return this.taskType;
        }

        private ArrayList<T> keyCodes;
        
        /**
         * KeyCodeを取得する.
         * @return KeyCode
         */
        public ArrayList<T> getKeyCodes() {
            return this.keyCodes;
        }
        
        private long milliseconds;
        
        /**
         * 待機時間を取得する.
         * @return milliseconds
         */
        public long getMilliseconds() {
            return this.milliseconds;
        }

    }
    
}

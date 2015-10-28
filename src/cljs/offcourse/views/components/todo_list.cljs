(ns offcourse.views.components.todo-list
  (:require [offcourse.helpers.css :as css]
            [quiescent.dom :as d]))


(defn Checkbox [completed {:keys [toggle-done]}]
  (let [completed (if completed "complete" "incomplete")]
    (d/span {:className (css/classes "checkbox" completed)
             :onClick toggle-done})))

(defn TodoListItem [[id completed highlighted title]
                    {:keys [toggle-done go-to-checkpoint highlight]}]
  (let [highlighted (if highlighted "highlighted" "not-highlighted")
        toggle-done (partial toggle-done id)
        go-to-checkpoint (partial go-to-checkpoint id)
        highlight (partial highlight id)]
    (d/li {:key id
           :className (css/classes "todolist_item" highlighted)
           :onMouseEnter #(highlight true)
           :onMouseLeave #(highlight false)}
          (d/p {}
               (Checkbox completed {:toggle-done toggle-done})
               (d/span {:onClick go-to-checkpoint} title)))))


(defn TodoList [id items {:keys [toggle-done go-to-checkpoint highlight]}]
  (let [toggle-done (partial toggle-done id)
        go-to-checkpoint (partial go-to-checkpoint id)
        highlight (partial highlight id)]
    (d/ul {:className (css/classes "todolist")}
          (for [[_ {:keys [id completed highlighted task]}] items]
            (TodoListItem [id completed highlighted task]
                          {:toggle-done toggle-done
                           :go-to-checkpoint go-to-checkpoint
                           :highlight highlight})))))

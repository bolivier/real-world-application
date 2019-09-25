(ns dev.user
  (:require [shadow.cljs.devtools.api :as shadow]))

;; This isn't being loaded by default in the ns
;; My suspicion is that THIS ns isn't being loaded AT ALL
(defn start []
  (shadow.cljs.devtools.api/watch :frontend))

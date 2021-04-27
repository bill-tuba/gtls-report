(ns badams.server-test
  (:require [badams.server :as sut]
            [clojure.test :refer :all]))

(deftest server-test
  (testing "port can be passed in and parsed"
    (let [state (atom nil)]
      (with-redefs [sut/start-server (fn [port _] (reset! state [:started! port]))]
        (sut/-main "-p" "1234"))
      (is (= [:started! 1234] @state))))

  (testing "port defaults if not passed in"
    (let [state (atom nil)]
      (with-redefs [sut/start-server (fn [port _] (reset! state [:started! port]))]
        (sut/-main))
      (is (= [:started! 8081] @state))))

  (testing "server fails to start a non-port value is used"
    (let [state (atom nil)]
      (with-redefs [sut/error (constantly (reset! state :failed!))]
        (sut/-main "-p" "TILT!"))
      (is (= :failed! @state)))))

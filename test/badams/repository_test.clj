(ns badams.repository-test
  (:require [badams.details :as details]
            [badams.repository :as sut]
            [clojure.test :refer :all]))

(deftest put!-values-test
  (let [repo    (sut/atomic-repo)
        items [{:1st 'Object}
               {:2nd 'Thing}
               {:3rd 'Item}]]

    (is (empty? (sut/values repo)))

    (doseq [it items]
      (sut/put! repo it))

    (is (= items
           (sut/values repo)
           @repo))))

(deftest sort-values-test
  (let [init-data [{:LastName "C"  :Email "E1" :DateOfBirth (details/date "3/3/2003")}
                   {:LastName "B1" :Email "E3" :DateOfBirth (details/date "1/1/2001")}
                   {:LastName "A"  :Email "E2" :DateOfBirth (details/date "1/1/2001")}
                   {:LastName "B2" :Email "E3" :DateOfBirth (details/date "2/2/2002")}]

        repo (sut/atomic-repo init-data)]

    (is (= ["C"
            "B2"
            "B1"
            "A"]
           (map :LastName (sut/values repo {:order sut/by-last-name-desc}))))

    (is (= ["1/1/2001"
            "1/1/2001"
            "2/2/2002"
            "3/3/2003"]
           (map (comp :DateOfBirth details/format-details)
                (sut/values repo {:order sut/by-birth-date-asc}))))

    (is (= [["E3" "B1"]
            ["E3" "B2"]
            ["E2" "A"]
            ["E1" "C"]]
           (map (juxt :Email :LastName)
                (sut/values repo {:order sut/by-email-desc-last-name-asc}))))))

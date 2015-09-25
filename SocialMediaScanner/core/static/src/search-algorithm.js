/**
 * Created by renl on 9/25/15.
 */
function searchKeywordFromData(searchKeyword, data) {
    var words = searchKeyword.split(" ");
    var totalWords = words.length;
    var rankMap = {};
    for (var i = 0; i < data.length; i++) {
        var content = data[i].content;
        var count = 0;
        for (var j = 0; j < totalWords; j++) {
            if (content.search(words[j]) != -1) {
                count++;
            }
        }
        if (count != 0) {
            if (!(count in rankMap)) {
                rankMap[count] = [];
            }
            rankMap[count].push(i);
        }
    }
    var filteredData = [];
    for (var m = totalWords; m > 0; m--) {
        if (m in rankMap) {
            var positionList = rankMap[m];
            for (var n = 0; n < positionList.length; n++) {
                filteredData.push(data[positionList[n]]);
            }
        }
    }
    return filteredData;
}

module.exports = searchKeywordFromData;
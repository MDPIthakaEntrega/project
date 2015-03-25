/**
 * Created by renl on 3/24/15.
 */

var allData = [];
var numberOfData = -1;
var workable = false;

function initializeNewReviewsState(data) {
    var reviewLookup = $.cookie('new_review');
    console.log(reviewLookup[0]);
    /*for (i = 0; i < data.length; i++) {
        if(!(data[i].review_id in reviewLookup)) {
            reviewLookup[data[i].review_id] = 0;
        }
    }*/
}
/*
function displayNewReviews(data) {
    var output = '';
    var reviewLookUp = $.cookie('new_review');
    for(i = 0; i < data.length; i++) {
        if(!(data[i].review_id in reviewLookUp)
            || (data[i].review_id in reviewLookUp && reviewLookUp[data[i].review_id] == 0)){
            output += '<div class="panel panel-default"><div class="panel-heading"><h4 class="panel-title">';
            output += data[i].review_title;
            output += '</div><div class="panel-body" style="font-size: 10px">';
            output += allData[i].review_text;
            output += '</div></div>';
        }
    }
    $('#new_results').html(output);
}
*/

$.ajax({
      url: '/static/data.json',
      dataType: 'json',
      success: function(data) {
          allData = data;
          numberOfData = allData.length;
          workable = true;
          initializeNewReviewsState(allData);
          //displayNewReviews(allData);
      },
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }
});

$(document).ready(function(){
    $("#filter").keyup(function(){
        var filter = $(this).val();
        if(workable) {
            if(filter != '') {
                var words = filter.split(" ");
                var totalWords = words.length;
                var rankMap = {};
                var output = '';
                for(i = 0; i < numberOfData; i++) {
                    var str = allData[i].review_text;
                    var count = 0;
                    for(j = 0; j < totalWords; j++) {
                        if (str.search(words[j]) != -1 ){
                            count++;
                        }
                    }
                    if (count != 0) {
                        if(!(count in rankMap)) {
                            rankMap[count] = [];
                        }
                        rankMap[count].push(i);
                    }
                }
                for (m = totalWords; m > 0; m--) {
                    if(m in rankMap) {
                        var positionList = rankMap[m];
                        for(n = 0; n < positionList.length; n++) {
                            output += '<div class="panel panel-default"><div class="panel-heading"><h3 class="panel-title">';
                            output += allData[positionList[n]].review_title;
                            output += '</div><div class="panel-body">';
                            output += allData[positionList[n]].review_text;
                            output += '</div></div>';
                        }
                    }
                }
                $('#results').html(output)
            }
            else {
                $('#results').html('');
            }
        }
    })
});



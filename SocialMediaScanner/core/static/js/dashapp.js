/**
 * Created by renl on 3/24/15.
 */

var allData = [];
var newData = [];
var numberOfData = -1;
var workable = false;

function retrieveAllData() {
    $.ajax({
      url: '/static/data.json',
      dataType: 'json',
      success: function(data) {
          allData = data;
          numberOfData = allData.length;
          workable = true;
      },
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }
    });
}

function getNewData() {
    var company_name = $("#usr").text();
    console.log(company_name);
    $.ajax({
      url: '/static/new_review_cookie/' + company_name + '_cookie.json',
      dataType: 'json',
      success: function(data) {
          newData = data;
      },
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }
    });
}

function newDataRender() {
    for (i = 0; i < newData.length; i++) {

    }
}

$(document).ready(function(){
    getNewData();
    retrieveAllData();
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



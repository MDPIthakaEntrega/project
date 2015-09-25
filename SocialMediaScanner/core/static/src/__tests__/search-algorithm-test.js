/**
 * Created by renl on 9/25/15.
 */
jest.dontMock('../search-algorithm');

describe('search-algorithm', function () {
    it('searches', function () {
        var mock_data = [
            {"content":"a b c d","title":"t1"},
            {"content":"a b","title":"t2"},
            {"content":"a b c asdf","title":"t3"},
            {"content":"a asdf","title":"t4"},
            {"content":"test","title":"t5"},
            {"content":"abc","title":"t6"},
            {"content":"abc","title":"t7"}
        ];
        var keyword_mock = "a b c d";
        var searchKeywordFromData = require('../search-algorithm');
        var result = searchKeywordFromData(keyword_mock, mock_data);
        expect(result[0].content).toBe("a b c d");
        expect(result[1].title).toBe("t3");
    });
});
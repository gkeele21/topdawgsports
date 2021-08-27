<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="golfMenu">
    <table width="50%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
        <tr>
            <c:if test="${menuLevel >= 1}">
                <a href="../fsGameList.htm">ALL GAMES</a>>>
            </c:if>
            <c:if test="${menuLevel >= 2}">
                <a href="../fsGameView.htm">FSGAME</a>>>
            </c:if>
            <c:if test="${menuLevel >= 3}">
                <a href="../fsSeasonView.htm">FSEASON</a>>>
            </c:if>
            <c:if test="${menuLevel >= 4}">
                <a href="golfTournamentWeeks.htm">ALL WEEKS</a>>>
            </c:if>
            <c:if test="${menuLevel >= 5}">
                <a href="golfTournamentWeek.htm">WEEK</a>
            </c:if>
        </tr>
        <c:if test="${menuLevel >= 4}">
            <tr height="22" class="rowData">
                <c:if test="${menuLevel >= 4}">
                    <td align="center"><a href="golfPlayers.htm">Golfers</a></td>
                </c:if>
                <c:if test="${menuLevel >= 5}">
                    <td align="center"><a href="golfTournamentWeekFieldEdit.htm">Set Field</a></td>
                    <td align="center"><a href="golfCalculateSalaryValues.htm">Calculate Salaries</a></td>
                    <td align="center"><a href="golfCalculateResults.htm">Calculate Results</a></td>
                </c:if>
<!--            <td align="center"><a href="golf/ImportWorldRanking.htm">Import World Golf Ranking</a></td>
                <td align="center"><a href="golf/RunLiveStats.htm">Run Live Stats</a></td>
                <td align="center"><a href="golf/GolfersNotInTournament.htm">Replace Golfers Not Playing</a></td>
                <td align="center"><a href="golf/EnterPlayerScores.htm">Enter Player Scores</a></td>
                <td align="center"><a href="golf/CalculateFantasyResults.htm">Calculate Fantasy Results</a></td> -->
            </tr>
        </c:if>
    </table>
</div>

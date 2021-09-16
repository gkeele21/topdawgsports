<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="gameMenu">
    <c:choose>
        <c:when test="${commFSSeason.FSGameID == 1}">
            <table width="50%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                <tr>
                    <td colspan="7" height="10"><img src="../images/spacer.gif" /></td>
                </tr>
                <tr class="rowHeader">
                    <td width="100" align="center">Pre-Season</td>
                    <td width="100" align="center">Mid-Season</td>
                    <td width="100" align="center">Post-Season</td>
                </tr>
                <tr height="22" class="rowData">
                    <td align="center"><a href="fantasy/fsSeason_updatePlayers.htm">Update Players</a></td>
                    <td align="center"><a href="fantasy/viewTransactionRequests.htm">View Transaction Requests</a></td>
                    <td align="center"></td>
                </tr>
                <tr height="22" class="rowData">
                    <td align="center"><a href="fantasy/fsLeague_modifyDraft.htm">Draft Results</a></td>
                    <td align="center"><a href="fantasy/fsSeason_figureStarters.htm">Figure Starters</a></td>
                    <td align="center"></td>
                </tr>
                <tr height="22" class="rowData">
                    <td align="center"><a href="fantasy/fsSeason_createRosters.htm">Create Rosters</a></td>
                    <td align="center"><a href="fantasy/fsSeason_seasonStats.htm">Total Season Stats</a></td>
                    <td align="center"></td>
                </tr>
                <tr height="22" class="rowData">
                    <td align="center"><a href="fantasy/fsSeason_createMatchups.htm">Create Matchups</a></td>
                    <td align="center"><a href="fantasy/fsSeason_calculateResults.htm">Calculate Results</a></td>
                    <td align="center"></td>
                </tr>
                <tr height="22" class="rowData">
                    <td align="center"></td>
                    <td align="center"><a href="fantasy/fsSeason_processTransactions.htm">Process Transactions</a></td>
                    <td align="center"></td>
                </tr>
            </table>
        </c:when>
        <c:when test="${commFSSeason.FSGameID == 2}">
            <table width="50%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                <tr>
                    <td colspan="7" height="10"><img src="../images/spacer.gif" /></td>
                </tr>
                <tr class="rowHeader">
                    <td width="100" align="center">Pre-Season</td>
                    <td width="100" align="center">Mid-Season</td>
                    <td width="100" align="center">Post-Season</td>
                </tr>
                <tr height="22" class="rowData">
                    <td align="center"></td>
                    <td align="center"><a href="sal/fsSeason_calculateResults.htm">Calculate Results</a></td>
                    <td align="center"></td>
                </tr>
                <tr height="22" class="rowData">
                    <td align="center"></td>
                    <td align="center"><a href="sal/fsSeason_createSalaries.htm">Create Salaries</a></td>
                    <td align="center"></td>
                </tr>
            </table>
        </c:when>
        <c:when test="${commFSSeason.FSGameID == 12}">
            <table width="50%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                <tr>
                    <td colspan="7" height="10"><img src="../images/spacer.gif" /></td>
                </tr>
                <tr height="22" class="rowData">
                    <td align="center"><a href="golf/golfTournaments.htm">Tournaments</a></td>
                    <td align="center"><a href="golf/golfTournamentWeeks.htm">Tournament Weeks</a></td>
                </tr>
            </table>
        </c:when>

    </c:choose>
</div>

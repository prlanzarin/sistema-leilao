package main.scala.presentation.gui.table

import java.util.Date

import main.scala.presentation.gui.table.event.TableColumnHeaderSelected

import scala.swing.Table
import scala.swing.event.{TableColumnsSelected, TableRowsSelected}

/**
 * Created by mhbackes on 25/05/15.
 */
class SortableTable(var rowData: Array[Array[Any]], headers: Seq[Any]) extends Table(rowData, headers) {
  selection.elementMode = Table.ElementMode.Row
  selection.intervalMode = Table.IntervalMode.Single

  var lastRowSorted = -1
  var reverseSort = false

  val header = {
    import java.awt.event.{MouseAdapter, MouseEvent}

    val makeHeaderEvent = TableColumnHeaderSelected(this, _: Int)
    val tableHeader = peer.getTableHeader
    tableHeader.addMouseListener(new MouseAdapter() {
      override def mouseClicked(e: MouseEvent) {
        selection.publish(makeHeaderEvent(tableHeader.columnAtPoint(e.getPoint)))
      }
    })
    tableHeader
  }

  listenTo(this.selection)

  reactions += {
    case TableRowsSelected(source, range, false) =>
    case TableColumnsSelected(source, range, false) =>
    case TableColumnHeaderSelected(source, column) =>
      if (column == lastRowSorted)
        reverseSort = !reverseSort
      else {
        reverseSort = false
        lastRowSorted = column
      }
      rowData = rowData.sortWith(sortByRow(column)(reverseSort))
      updateCells
    //case e => println("%s => %s" format(e.getClass.getSimpleName, e.toString))
  }

  def sortByRow(row: Int)(reverse: Boolean)(a: Array[Any], b: Array[Any]): Boolean = { //FIXME only sorts strings
    if (reverse)
      (a(row), b(row)) match {
        case (x: Double, y: Double) => x > y
        case (x: Int, y: Int) => x > y
        case (x: Date, y: Date) => x.compareTo(y) > 0
        case (_, _) => a(row).toString.toUpperCase > b(row).toString.toUpperCase
      }
    else
      (a(row), b(row)) match {
        case (x: Double, y: Double) => x < y
        case (x: Int, y: Int) => x < y
        case (x: Date, y: Date) => x.compareTo(y) < 0
        case (_, _) => a(row).toString.toUpperCase < b(row).toString.toUpperCase
      }
  }

  def updateCells {
    val clone = rowData.map(_.clone)
    for (i <- 0 until rowData.size) {
      for (j <- 0 until headers.size) {
        this(i, j) = clone(i)(j)
      }
    }
  }
}


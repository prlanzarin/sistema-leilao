package main.scala.presentation.gui.table.event

import scala.swing.Table
import scala.swing.event.TableEvent

/**
 * Created by mhbackes on 25/05/15.
 */
case class TableColumnHeaderSelected(override val source: Table, column: Int) extends TableEvent(source)

package jtkaiser.imags.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import jtkaiser.imags.MedicationData;
import jtkaiser.imags.database.DbSchema.MedicationTable;

/**
 * Created by jtkai on 3/25/2018.
 */


public class MedicationDataCursorWrapper extends CursorWrapper {
    public MedicationDataCursorWrapper(Cursor cursor){
        super(cursor);
    }

        public MedicationData getMedData(){
            MedicationData data = new MedicationData();
            data.tookMed = getInt(getColumnIndex(MedicationTable.Cols.TOOK));
            data.medName = getString(getColumnIndex(MedicationTable.Cols.MED));
            data.dosage = getString(getColumnIndex(MedicationTable.Cols.DOSE));

            return data;
        }
    }


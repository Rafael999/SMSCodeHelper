/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
class aca
implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ abu b;

    aca(abu abu2, String string2) {
        this.b = abu2;
        this.a = string2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void run() {
        String string2;
        try {
            string2 = abu.a(this.a, abu.c(this.b));
            if (string2 == null) return;
        }
        catch (Exception var1_2) {
            var1_2.printStackTrace();
            return;
        }
        abu.e(this.b).post((Runnable)new acb(this, string2));
    }
}
